package com.openbravo.pos.payment.ccv.interfaces;

import com.openbravo.pos.payment.ccv.service.SocketMessage;

public interface ICCVListener {
    void messageReceived(SocketMessage event);
}
