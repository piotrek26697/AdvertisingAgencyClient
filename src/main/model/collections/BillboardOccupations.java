package main.model.collections;

import main.model.entities.BillboardOccupation;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class BillboardOccupations
{
    private List<BillboardOccupation> list;

    public List<BillboardOccupation> getList()
    {
        return list;
    }

    public void setList(List<BillboardOccupation> list)
    {
        this.list = list;
    }
}
