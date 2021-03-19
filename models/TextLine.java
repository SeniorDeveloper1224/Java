package com.openbravo.pos.payment.ccv.models;

import javax.xml.bind.annotation.*;

@XmlType(name= "TextLine", namespace = "http://www.nrf-arts.org/IXRetail/namespace")
public class TextLine {
    public TextLine(){
    }
    
    @XmlAttribute(name="Height")
    public String Height;
    
    @XmlAttribute(name="Width")
    public String Width;
    
    @XmlValue
    public String Text;
    
}
