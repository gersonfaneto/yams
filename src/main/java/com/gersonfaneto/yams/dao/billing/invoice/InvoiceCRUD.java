package com.gersonfaneto.yams.dao.billing.invoice;

import com.gersonfaneto.yams.dao.CRUD;
import com.gersonfaneto.yams.models.billing.invoice.Invoice;

/**
 * Extends the <code>CRUD</code> interface by adding operations specific to the <code>Invoice</code>
 * models.
 *
 * @see CRUD
 * @see Invoice
 */
public interface InvoiceCRUD extends CRUD<Invoice> {

  /**
   * Searches for a <code>Invoice</code> by its referred <code>WorkOrder</code>.
   *
   * @param workOrderID The ID of the targeted <code>WorkOrder</code>.
   * @return The <code>Invoice</code> itself if found, or <code>null</code> if not.
   */
  Invoice findByWorkOrder(String workOrderID);
}
