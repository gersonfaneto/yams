package com.gersonfaneto.yams.controllers;

import java.io.IOException;
import java.util.List;

import com.gersonfaneto.yams.App;
import com.gersonfaneto.yams.dao.DAO;
import com.gersonfaneto.yams.models.entities.client.Client;
import com.gersonfaneto.yams.models.orders.work.WorkOrder;
import com.gersonfaneto.yams.models.orders.work.states.StateType;
import com.gersonfaneto.yams.models.services.Service;
import com.gersonfaneto.yams.models.services.ServiceType;
import com.gersonfaneto.yams.models.stock.Component;
import com.gersonfaneto.yams.models.stock.ComponentType;
import com.gersonfaneto.yams.utils.TypeParser;
import com.gersonfaneto.yams.views.components.OrdersListComponent;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class ServicesController {

  @FXML
  private FontAwesomeIconView closeButton;

  @FXML
  private ListView<WorkOrder> listView;

  @FXML
  private TextField searchField;

  @FXML
  private Button registerButton;

  @FXML
  private ComboBox<String> statusFilter;

  private ObservableList<WorkOrder> workOrdersList;
  private FilteredList<WorkOrder> filteredWorkOrders;

  @FXML
  public void initialize() {
    if (DAO.fromService().findMany().size() == 0) {
      Client randomClient = DAO.fromClients().createOne(
          new Client(
            "Gerson Ferreira dos Anjos Neto",
            "Rua A, Campo Limpo, 07, Feira de Santana",
            "(11) 91234-1234"
            )
          );

      WorkOrder newWorkOrder = DAO.fromWorkOrders().createOne(new WorkOrder(randomClient.getClientID()));

      Service foo = new Service(ServiceType.Cleaning, "Dusty!", null, 0);
      Service bar = new Service(ServiceType.Formatting, "Install Ubuntu 22.04", null, 0);
      Service baz = new Service(ServiceType.ProgramInstallation, "Install Google Chrome", null, 0);
      Service fizz = new Service(
          ServiceType.Assembly,
          "Add RAM",
          DAO.fromComponents().createOne(new Component(ComponentType.RAM, "RAM", 1, 20, 15)),
          1
      );

      foo.setWorkOrderID(newWorkOrder.getWorkOrderID());
      bar.setWorkOrderID(newWorkOrder.getWorkOrderID());
      baz.setWorkOrderID(newWorkOrder.getWorkOrderID());
      fizz.setWorkOrderID(newWorkOrder.getWorkOrderID());

      DAO.fromService().createOne(foo);
      DAO.fromService().createOne(bar);
      DAO.fromService().createOne(baz);
      DAO.fromService().createOne(fizz);
    }

    workOrdersList = FXCollections.observableArrayList();
    filteredWorkOrders = new FilteredList<>(workOrdersList);

    statusFilter.getItems().add("Todos");
    for (StateType componentType : StateType.values()) {
      statusFilter.getItems().add(TypeParser.parseWorkOrderStateType(componentType));
    }

    listView.setCellFactory(listView -> new ListCell<WorkOrder>() {
      @Override
      protected void updateItem(WorkOrder workOrder, boolean empty) {
        super.updateItem(workOrder, empty);

        if (workOrder == null || empty) {
          setGraphic(null);
        } else {
          OrdersListComponent orderComponent = new OrdersListComponent(
              workOrder,
              workOrdersList
          );
          setGraphic(orderComponent);
        }
      }
    });

    List<WorkOrder> allComponents = DAO.fromWorkOrders().findMany();

    workOrdersList.addAll(allComponents);

    searchField.textProperty().addListener((observable, oldValue, newValue) -> {
      filteredWorkOrders.setPredicate(workOrder -> {
        if (newValue == null || newValue.isEmpty()) {
          return true;
        }

        String lowerCaseFilter = newValue.toLowerCase();

        if (DAO.fromClients().findByID(workOrder.getClientID()).getClientName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
          return true;
        }
        else {
          return false;
        }
      });
    });

    SortedList<WorkOrder> sortedWorkOrders = new SortedList<>(filteredWorkOrders);

    listView.setItems(sortedWorkOrders);
  }
  
  @FXML
  public void filterSearch() {
    listView.setItems(filteredWorkOrders.filtered(workOrder -> {
      String statusValue = statusFilter.getValue();

      StateType statusType = TypeParser.parseWorkOrderStateType(statusValue);

      return statusType == null || workOrder.getWorkOrderStateType() == statusType;
    }));
  }

  @FXML
  public void closeWindow() {
    MainController.saveData();
    System.exit(0);
  }

  @FXML
  public void registerOrder() throws IOException {
    Parent clientRegisterView = FXMLLoader.load(
        App.class.getResource("views/create_order.fxml")
    );

    MainController.mainWindow.setRight(clientRegisterView);
  }
}

