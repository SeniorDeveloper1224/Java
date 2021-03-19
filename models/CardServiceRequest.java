/**
* Domain classes used to produce .....
* <p>
* These classes contain the ......
* </p>
*
* @since 1.0
* @author somebody
* @version 1.0
*/
package com.openbravo.pos.payment.ccv.models;

import com.openbravo.pos.payment.ccv.enums.EnumCardServiceRequestType;
import com.openbravo.pos.payment.ccv.service.SocketMessage;
import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import javax.xml.bind.annotation.*;

@XmlRootElement(name = "CardServiceRequest")
public class CardServiceRequest extends SocketMessage {
    public CardServiceRequest(){}
    public CardServiceRequest(String workstationID, int requestID, EnumCardServiceRequestType requestType, POSdata posData, com.openbravo.pos.payment.ccv.models.TotalAmount totalAmount){
        this.WorkstationID = workstationID;
        this.RequestID = requestID;
        this.RequestType = requestType;
        this.POSData = posData;
        this.TotalAmount = totalAmount;
        this.CardCircuitCollection = "";
    }
    
    @XmlAttribute(name="WorkstationID")
    public String WorkstationID;
    @XmlAttribute(name="RequestID")
    public int RequestID;
    @XmlAttribute(name="RequestType")
    public EnumCardServiceRequestType RequestType;
    @XmlElement(name="POSdata")
    public POSdata POSData;
    @XmlElement(name="TotalAmount")
    private TotalAmount TotalAmount;
    @XmlElement(name="CardCircuitCollection")
    private String CardCircuitCollection;
      
    @Override
    public String toString() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (XMLEncoder encoder = new XMLEncoder(outputStream)) {
            encoder.writeObject(CardServiceRequest.this);
        }
        return new String(outputStream.toByteArray());
    }
}
