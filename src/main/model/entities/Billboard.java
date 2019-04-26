package main.model.entities;

import main.model.BillboardSize;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class Billboard
{
    private int id;

    private String address;

    private List<BillboardOccupation> billboardOccupationList;

    private BillboardSize billboardSize;

    public Billboard()
    {
        billboardOccupationList = new ArrayList<>();
    }

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

    @XmlTransient
    public List<BillboardOccupation> getBillboardOccupationList()
    {
        return billboardOccupationList;
    }

    public void setBillboardOccupationList(List<BillboardOccupation> billboardOccupationList)
    {
        this.billboardOccupationList = billboardOccupationList;
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
