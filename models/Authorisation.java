package com.openbravo.pos.payment.ccv.models;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.util.Date;

@XmlType(name= "Authorisation", namespace = "http://www.nrf-arts.org/IXRetail/namespace")
public class Authorisation {
    public Authorisation(){

    }

    @XmlAttribute(name="TimeStamp")
    public Date TimeStamp;

    @XmlAttribute(name="ApprovalCode")
    public String ApprovalCode;

    @XmlAttribute(name="MaskedCardNumber")
    public String MaskedCardNumber;

    @XmlAttribute(name="AcquirerBatch")
    public String AcquirerBatch;

    @XmlAttribute(name="CardCircuit")
    public String CardCircuit;

    @XmlAttribute(name="ReceiptCopies")
    public int ReceiptCopies;
}


