package com.gersonfaneto.yams.dao.orders.purchase;

import com.gersonfaneto.yams.dao.CRUD;
import com.gersonfaneto.yams.models.orders.purchase.PurchaseOrder;
import com.gersonfaneto.yams.models.stock.ComponentType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Implementations for the <code>PurchaseOrderCRUD</code> and <code>CRUD</code> operations. Uses a
 * <code>HashMap</code> to store all the <code>PurchaseOrder</code>s.
 *
 * @author Gerson Ferreira dos Anjos Neto
 * @version 1.0.0
 * @see CRUD
 * @see PurchaseOrderCRUD
 */
public class PurchaseOrderMemoryDAO implements PurchaseOrderCRUD {

  private final Map<String, PurchaseOrder> storedPurchaseOrders;

  /** Initializes the <code>HashMap</code> used to store all the <code>PurchaseOrder</code>s. */
  public PurchaseOrderMemoryDAO() {
    this.storedPurchaseOrders = new HashMap<>();
  }

  @Override
  public PurchaseOrder createOne(PurchaseOrder newPurchaseOrder) {
    String newID = UUID.randomUUID().toString();

    newPurchaseOrder.setPurchaseOrderID(newID);

    storedPurchaseOrders.put(newID, newPurchaseOrder);

    return newPurchaseOrder;
  }

  @Override
  public PurchaseOrder findByID(String targetID) {
    return storedPurchaseOrders.get(targetID);
  }

  @Override
  public List<PurchaseOrder> findMany() {
    return storedPurchaseOrders.values().stream().toList();
  }

  @Override
  public boolean updateInformation(PurchaseOrder updatedPurchaseOrder) {
    String purchaseOrderID = updatedPurchaseOrder.getPurchaseOrderID();

    if (storedPurchaseOrders.containsKey(purchaseOrderID)) {
      storedPurchaseOrders.put(purchaseOrderID, updatedPurchaseOrder);
      return true;
    }

    return false;
  }

  @Override
  public boolean deleteByID(String targetID) {
    if (storedPurchaseOrders.containsKey(targetID)) {
      storedPurchaseOrders.remove(targetID);
      return true;
    }

    return false;
  }

  @Override
  public boolean deleteMany() {
    if (!storedPurchaseOrders.isEmpty()) {
      storedPurchaseOrders.clear();
      return true;
    }

    return false;
  }

  @Override
  public List<PurchaseOrder> findByType(ComponentType componentType) {
    return storedPurchaseOrders.values().stream()
        .filter(x -> x.getComponentType().equals(componentType))
        .toList();
  }
}
