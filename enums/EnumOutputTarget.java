package com.openbravo.pos.payment.ccv.enums;

public enum EnumOutputTarget {
    Printer, CustomerDisplay, CashierDisplay, EJournal, JournalPrinter;
    public String toString(){
        switch (this) {
            case Printer: return "Printer";
            case CustomerDisplay: return "CustomerDisplay";
            case CashierDisplay: return "CashierDisplay";
            case EJournal: return "E-Journal";
            case JournalPrinter: return "JournalPrinter";
        }
        return null;
    }
}
