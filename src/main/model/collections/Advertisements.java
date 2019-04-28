package main.model.collections;

import main.model.entities.Advertisement;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class Advertisements
{
    private List<Advertisement> adsList;

    public Advertisements()
    {
        adsList = new ArrayList<>();
    }

    public List<Advertisement> getAdsList()
    {
        return adsList;
    }

    public void setAdsList(List<Advertisement> adsList)
    {
        this.adsList = adsList;
    }
}
