package main.model.entities;

import main.model.BillboardSize;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Billboard
{
    private int id;

    private String address;

    private Advertisement advertisementDisplayed;

    private BillboardSize billboardSize;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public Advertisement getAdvertisementDisplayed()
    {
        return advertisementDisplayed;
    }

    public void setAdvertisementDisplayed(Advertisement advertisementDisplayed)
    {
        this.advertisementDisplayed = advertisementDisplayed;
    }

    public BillboardSize getBillboardSize()
    {
        return billboardSize;
    }

    public void setBillboardSize(BillboardSize billboardSize)
    {
        this.billboardSize = billboardSize;
    }
}
