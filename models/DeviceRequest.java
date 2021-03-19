/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.payment.ccv.models;

import com.openbravo.pos.payment.ccv.enums.EnumCardServiceRequestType;
import com.openbravo.pos.payment.ccv.service.SocketMessage;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "DeviceRequest",namespace = "http://www.nrf-arts.org/IXRetail/namespace")
public class DeviceRequest extends SocketMessage {

    public DeviceRequest(){
    }
    
    @XmlAttribute(name="WorkstationID")
    public String WorkstationID;//POS
    
    @XmlAttribute(name="RequestID")
    public String RequestID;//160
    
    @XmlAttribute(name="RequestType")
    public EnumCardServiceRequestType RequestType;

    @XmlElement(name="Output", namespace = "http://www.nrf-arts.org/IXRetail/namespace")
    public Output Output;
}
