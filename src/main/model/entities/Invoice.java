package main.model.entities;

import main.model.LocalDateAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Invoice
{
    private int id;

    private double amountNetto;

    private double amountBrutto;

    private int tax = 23;

    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate date;

    @XmlTransient
    private List<Advertisement> advertisementList;

    public Invoice()
    {
        advertisementList = new ArrayList<>();
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public double getAmountNetto()
    {
        return amountNetto;
    }

    public void setAmountNetto(double amountNetto)
    {
        this.amountNetto = amountNetto;
    }

    public int getTax()
    {
        return tax;
    }

    public void setTax(int tax)
    {
        this.tax = tax;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public void setDate(LocalDate date)
    {
        this.date = date;
    }

    public List<Advertisement> getAdvertisementList()
    {
        return advertisementList;
    }

    public void setAdvertisementList(List<Advertisement> advertisementList)
    {
        this.advertisementList = advertisementList;
    }

    public double getAmountBrutto()
    {
        return amountBrutto;
    }

    public void setAmountBrutto(double amountBrutto)
    {
        this.amountBrutto = amountBrutto;
    }
}