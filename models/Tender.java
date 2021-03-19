package com.openbravo.pos.payment.ccv.models;

import javax.xml.bind.annotation.*;

@XmlType(name= "Tender", namespace = "http://www.nrf-arts.org/IXRetail/namespace")
public class Tender {

    public Tender(){}

    @XmlAttribute(name="LanguageCode")
    public String LanguageCode;//nl of en

    @XmlElement(name="TotalAmount",namespace = "http://www.nrf-arts.org/IXRetail/namespace")
    private TotalAmount TotalAmount;

    @XmlElement(name="Authorisation",namespace = "http://www.nrf-arts.org/IXRetail/namespace")
    private Authorisation Authorisation;
}
