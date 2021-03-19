package com.openbravo.pos.payment.ccv.models;

import com.openbravo.pos.payment.ccv.enums.EnumCardServiceRequestType;
import com.openbravo.pos.payment.ccv.enums.EnumOverallResult;
import com.openbravo.pos.payment.ccv.service.SocketMessage;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "DeviceResponse",namespace = "http://www.nrf-arts.org/IXRetail/namespace")
public class DeviceResponse extends SocketMessage {
    public DeviceResponse(){}

    /**
     * @param workstationID
     * @param requestID
     * @param requestType
     * @param cardServiceOutput
     * @param enumOverallResult
     */
    public DeviceResponse(String workstationID, String requestID, EnumCardServiceRequestType requestType, Output cardServiceOutput, EnumOverallResult enumOverallResult){
        this.WorkstationID = workstationID;
        this.RequestID = requestID;
        this.RequestType = requestType;
        this.EnumOverallResult = enumOverallResult;
        this.cardServiceOutput = cardServiceOutput;
    }

    @XmlAttribute(name = "WorkstationID")
    public String WorkstationID;

    @XmlAttribute(name = "RequestID")
    public String RequestID;

    @XmlAttribute(name = "RequestType")
    public EnumCardServiceRequestType RequestType;

    @XmlAttribute(name = "OverallResult")
    public EnumOverallResult EnumOverallResult;

    @XmlElement(name = "Output", namespace = "http://www.nrf-arts.org/IXRetail/namespace")
    public Output cardServiceOutput;
}
