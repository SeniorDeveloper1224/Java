package com.openbravo.pos.payment.ccv.models;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType(name= "Terminal", namespace = "http://www.nrf-arts.org/IXRetail/namespace")
public class Terminal {

    public Terminal(){}

    @XmlAttribute(name="TerminalID")
    public String TerminalID;

    @XmlAttribute(name="STAN")
    public String Stan;
}
