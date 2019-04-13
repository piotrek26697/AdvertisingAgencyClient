package main.model.entities;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Calendar;
import java.util.List;

@XmlRootElement
public class Advertisement
{
    private int id;

    private Calendar dateFrom;

    private Calendar dateTo;

    private double price;

    private Client client;

    private Invoice invoice;

    private List<Billboard> billboardList;
}
