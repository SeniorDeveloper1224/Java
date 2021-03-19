/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.payment.ccv.models;

import com.openbravo.pos.payment.ccv.enums.EnumAdapter;
import com.openbravo.pos.payment.ccv.enums.EnumOutResult;
import com.openbravo.pos.payment.ccv.enums.EnumOutputTarget;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;

@XmlType(name= "Output", namespace = "http://www.nrf-arts.org/IXRetail/namespace")
public class Output {
    public Output(){
    }

    /**
     * @param outDeviceTarget
     * @param outResult
     */
    public Output(EnumOutputTarget outDeviceTarget, EnumOutResult outResult){
        this.OutDeviceTarget = outDeviceTarget;
        this.OutResult = outResult;
    }

    @XmlJavaTypeAdapter(EnumAdapter.class)
    @XmlAttribute(name="OutDeviceTarget")
    public EnumOutputTarget OutDeviceTarget;

    @XmlAttribute(name="RequestID")
    public int ReceiptCopies;

    @XmlAttribute(name="OutResult")
    public EnumOutResult OutResult;

    @XmlElement(name="TextLine",namespace = "http://www.nrf-arts.org/IXRetail/namespace")
    public List<TextLine> TextLines;


}
