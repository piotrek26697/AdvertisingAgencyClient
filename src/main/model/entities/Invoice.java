package main.model.entities;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Calendar;
import java.util.List;

@XmlRootElement
public class Invoice
{
    private int id;

    private double amount;

    private int tax;

    private Calendar date;

    private List<Order> orderList;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public double getAmount()
    {
        return amount;
    }

    public void setAmount(double amount)
    {
        this.amount = amount;
    }

    public int getTax()
    {
        return tax;
    }

    public void setTax(int tax)
    {
        this.tax = tax;
    }

    public Calendar getDate()
    {
        return date;
    }

    public void setDate(Calendar date)
    {
        this.date = date;
    }

    public List<Order> getOrderList()
    {
        return orderList;
    }

    public void setOrderList(List<Order> orderList)
    {
        this.orderList = orderList;
    }
}