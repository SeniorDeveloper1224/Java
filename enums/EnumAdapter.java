package com.openbravo.pos.payment.ccv.enums;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class EnumAdapter extends XmlAdapter<String, EnumOutputTarget> {
    @Override
    public EnumOutputTarget unmarshal(String v) throws Exception {
        return EnumOutputTarget.valueOf(v);
    }

    @Override
    public String marshal(EnumOutputTarget v) throws Exception {
        return v.toString();
    }
}
