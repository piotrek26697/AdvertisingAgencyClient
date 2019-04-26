package main.model.collections;

import main.model.entities.Client;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class Clients implements Serializable
{
    private List<Client> clients;

    public Clients()
    {
        clients = new ArrayList<>();
    }

    public List<Client> getClients()
    {
        return clients;
    }

    public void setClients(List<Client> clients)
    {
        this.clients = clients;
    }
}