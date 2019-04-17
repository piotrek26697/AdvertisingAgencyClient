package main.model.entities;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class Advertisement
{
    private int id;

    private String description;

    private double price;

    private Client client;

    private List<Invoice> invoiceList;

    private List<Billboard> billboardList;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public double getPrice()
    {
        return price;
    }

    public void setPrice(double price)
    {
        this.price = price;
    }

    public Client getClient()
    {
        return client;
    }

    public void setClient(Client client)
    {
        this.client = client;
    }

    public List<Billboard> getBillboardList()
    {
        return billboardList;
    }

    public void setBillboardList(List<Billboard> billboardList)
    {
        this.billboardList = billboardList;
    }

    public List<Invoice> getInvoiceList()
    {
        return invoiceList;
    }

    public void setInvoiceList(List<Invoice> invoiceList)
    {
        this.invoiceList = invoiceList;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }
}
