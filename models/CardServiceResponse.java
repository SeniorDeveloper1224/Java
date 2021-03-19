package com.openbravo.pos.payment.ccv.models;

import com.openbravo.pos.payment.ccv.enums.EnumCardServiceRequestType;
import com.openbravo.pos.payment.ccv.enums.EnumOverallResult;
import com.openbravo.pos.payment.ccv.service.SocketMessage;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "CardServiceResponse",namespace = "http://www.nrf-arts.org/IXRetail/namespace")
public class CardServiceResponse extends SocketMessage {
    public void CardServiceResponse(){}

    @XmlAttribute(name="WorkstationID")
    public String WorkstationID;

    @XmlAttribute(name="RequestID")
    public int RequestID;

    @XmlAttribute(name="RequestType")
    public EnumCardServiceRequestType RequestType;

    @XmlAttribute(name = "OverallResult")
    public EnumOverallResult OverallResult;

    @XmlElement(name="Terminal",namespace = "http://www.nrf-arts.org/IXRetail/namespace")
    private Terminal Terminal;

    @XmlElement(name="Tender",namespace = "http://www.nrf-arts.org/IXRetail/namespace")
    private Tender Tender;
}
