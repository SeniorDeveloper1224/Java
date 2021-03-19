package com.openbravo.pos.payment.ccv.models;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlType(name= "TotalAmount", namespace = "http://www.nrf-arts.org/IXRetail/namespace")
public class TotalAmount {
        public TotalAmount(){
        }
        public TotalAmount(double amount){
            this.Amount = amount;
            this.Currency  = "EUR";
        }
        
        @XmlAttribute(name="Currency")
        public String Currency;//EUR
        
        @XmlValue
        public double Amount;
}
