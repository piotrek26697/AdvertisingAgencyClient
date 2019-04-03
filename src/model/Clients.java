package model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class Clients
{
    private List<Client> clients;

    public Clients(List<Client> clients)
    {
        this.clients = clients;
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