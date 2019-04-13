package main.model.entities;

import main.model.BillboardSize;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Billboard
{
    private String address;

    private Advertisement advertisementDisplayed;

    private BillboardSize billboardSize;
}
