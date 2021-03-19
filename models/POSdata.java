package com.openbravo.pos.payment.ccv.models;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.joda.time.DateTime;

@XmlType(name= "POSdata", namespace = "http://www.nrf-arts.org/IXRetail/namespace")
public class POSdata {
        public POSdata(){
        }

    /**
     * @param languageCode
     * @param shiftNumber
     * @param clerkID
     * @param printerStatus
     * @param eJournalStatus
     * @param journalPrinterStatus
     */
        public POSdata(String languageCode, int shiftNumber, int clerkID, String printerStatus, String eJournalStatus, String journalPrinterStatus){
            this.LanguageCode = languageCode;
            this.ShiftNumber= shiftNumber;
            this.ClerkID = clerkID;
            this.PrinterStatus = printerStatus;
            this.EJournalStatus = eJournalStatus;
            this.JournalPrinterStatus = journalPrinterStatus;
            this.POSTimeStamp = DateTime.now().toString("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        }
    
        @XmlAttribute(name="LanguageCode")
        public String LanguageCode;//nl of en
        
        @XmlElement
        public String POSTimeStamp; //2009-12-24T09:42:53.6324624+01:00

        @XmlElement
        public int ShiftNumber; //0

        @XmlElement
        public int ClerkID; //0

        @XmlElement
        public String PrinterStatus; //Available
        
        @XmlElement(name = "E-JournalStatus")
        public String EJournalStatus;//Available
        
        @XmlElement
        public String JournalPrinterStatus; //Available
}
