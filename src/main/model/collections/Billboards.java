package main.model.collections;

import main.model.entities.Billboard;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class Billboards
{
    private List<Billboard> billboardList;

    public Billboards()
    {
        billboardList = new ArrayList<>();
    }

    public List<Billboard> getBillboardList()
    {
        return billboardList;
    }

    public void setBillboardList(List<Billboard> billboardList)
    {
        this.billboardList = billboardList;
    }
}
