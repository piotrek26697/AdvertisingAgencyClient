package main.model.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Client implements Serializable
{
    private int id;

    private String name;

    private String lastName;

    private String address;

    @XmlTransient
    private List<Advertisement> adsList;

    public Client()
    {
        adsList = new ArrayList<>();
    }

    public Client(String name, String lastName, String address)
    {
        this.name = name;
        this.lastName = lastName;
        this.address = address;
        adsList = new ArrayList<>();
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
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