package main.model.collections;

import main.model.entities.Invoice;

import java.util.List;

public class Invoices
{
    private List<Invoice> invoiceList;

    public List<Invoice> getInvoiceList()
    {
        return invoiceList;
    }

    public void setInvoiceList(List<Invoice> invoiceList)
    {
        this.invoiceList = invoiceList;
    }
}
