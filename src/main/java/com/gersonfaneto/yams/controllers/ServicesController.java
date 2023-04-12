package com.gersonfaneto.yams.controllers;

import com.gersonfaneto.yams.dao.DAO;
import com.gersonfaneto.yams.exceptions.client.ClientNotFoundException;
import com.gersonfaneto.yams.exceptions.services.ServiceNotFound;
import com.gersonfaneto.yams.exceptions.services.ServiceTypeNotFound;
import com.gersonfaneto.yams.exceptions.orders.UnsupportedOperationException;
import com.gersonfaneto.yams.exceptions.users.UserNotFoundException;
import com.gersonfaneto.yams.exceptions.orders.WorkOrderNotFound;
import com.gersonfaneto.yams.models.stock.Component;
import com.gersonfaneto.yams.models.entities.client.Client;
import com.gersonfaneto.yams.models.entities.technician.Technician;
import com.gersonfaneto.yams.models.orders.work.WorkOrder;
import com.gersonfaneto.yams.models.services.Service;
import com.gersonfaneto.yams.models.services.ServiceType;
import java.util.Comparator;
import java.util.List;

public abstract class ServicesController {

  public static WorkOrder createWorkOrder(
      String clientName,
      List<Service> chosenServices
  ) throws ClientNotFoundException {
    Client foundClient = DAO.fromClients().findByName(clientName);

    if (foundClient == null) {
      throw new ClientNotFoundException("Client not found!");
    }

    WorkOrder workOrder = new WorkOrder(foundClient.getClientID());

    for (Service currentService : chosenServices) {
      currentService.setWorkOrderID(workOrder.getWorkOrderID());
      DAO.fromService().updateInformation(currentService);
    }

    return DAO.fromWorkOrders().createOne(workOrder);
  }

  public static Service createService(
      String serviceType,
      String serviceDescription,
      List<Component> usedComponents
  ) throws ServiceTypeNotFound {
    if (ServiceType.findByName(serviceType) == null) {
      throw new ServiceTypeNotFound("Service type not found!");
    }

    Service newService = new Service(
        ServiceType.findByName(serviceType),
        serviceDescription,
        usedComponents
    );

    return DAO.fromService().createOne(newService);
  }

  public static Service removeService(String workOrderID, String serviceID)
      throws ServiceNotFound, WorkOrderNotFound, UnsupportedOperationException {
    WorkOrder foundWorkOrder = DAO.fromWorkOrders().findByID(workOrderID);
    Service foundService = DAO.fromService().findByID(serviceID);

    if (foundWorkOrder == null) {
      throw new WorkOrderNotFound("Order not found!");
    }

    if (foundService == null) {
      throw new ServiceNotFound("Service not found!");
    }

    if (foundService.isComplete()) {
      throw new UnsupportedOperationException("Service is already complete!");
    }

    Service removedService = foundWorkOrder.removeService(serviceID);

    if (removedService == null) {
      throw new UnsupportedOperationException(
          "Order current state doesn't support removal of service!"
      );
    }

    DAO.fromWorkOrders().updateInformation(foundWorkOrder);

    return removedService;
  }

  public static List<Service> listServices(String workOrderID) throws WorkOrderNotFound {
    WorkOrder foundWorkOrder = DAO.fromWorkOrders().findByID(workOrderID);

    if (foundWorkOrder == null) {
      throw new WorkOrderNotFound("Order not found!");
    }

    return DAO.fromService().findByWorkOrder(workOrderID);
  }

  public static Service markAsDone(String serviceID) throws ServiceNotFound {
    Service foundService = DAO.fromService().findByID(serviceID);

    if (foundService == null) {
      throw new ServiceNotFound("Service not found!");
    }

    foundService.setComplete(true);

    DAO.fromService().updateInformation(foundService);

    return foundService;
  }

  public static WorkOrder openWorkOrder(
      String technicianID
  ) throws UserNotFoundException, WorkOrderNotFound, UnsupportedOperationException {
    Technician foundTechnician = (Technician) DAO.fromUsers().findByID(technicianID);

    if (DAO.fromUsers().findByID(technicianID) == null) {
      throw new UserNotFoundException("User not found");
    }

    WorkOrder foundWorkOrder = DAO.fromWorkOrders()
        .findMany()
        .stream()
        .filter(x -> x.getClosedAt() == null)
        .min(Comparator.comparing(WorkOrder::getCreatedAt))
        .stream()
        .findFirst()
        .orElse(null);

    if (foundWorkOrder == null) {
      throw new WorkOrderNotFound("No order available at the moment!");
    }

    if (!foundTechnician.openOrder(foundWorkOrder)) {
      throw new UnsupportedOperationException("You already have an order open!");
    }

    DAO.fromWorkOrders().updateInformation(foundWorkOrder);

    return foundWorkOrder;
  }

  public static WorkOrder closeWorkOrder(
      String technicianID
  ) throws WorkOrderNotFound, UserNotFoundException, UnsupportedOperationException {
    Technician foundTechnician = (Technician) DAO.fromUsers().findByID(technicianID);

    if (foundTechnician == null) {
      throw new UserNotFoundException("User not found!");
    }

    WorkOrder foundWorkOrder = foundTechnician.getTechnicianState().getWorkOrder();

    if (!foundTechnician.closeOrder()) {
      throw new UnsupportedOperationException("You don't have an order open!");
    }

    DAO.fromWorkOrders().updateInformation(foundWorkOrder);

    return foundWorkOrder;
  }

  public static WorkOrder cancelWorkOrder(
      String technicianID
  ) throws UserNotFoundException, UnsupportedOperationException {
    Technician foundTechnician = (Technician) DAO.fromUsers().findByID(technicianID);

    if (foundTechnician == null) {
      throw new UserNotFoundException("User not found!");
    }

    WorkOrder foundWorkOrder = foundTechnician.getTechnicianState().getWorkOrder();

    if (!foundTechnician.cancelOrder()) {
      throw new UnsupportedOperationException("You don't have an order open!");
    }

    DAO.fromWorkOrders().updateInformation(foundWorkOrder);

    return foundWorkOrder;
  }
}
