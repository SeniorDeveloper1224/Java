package com.openbravo.pos.payment.ccv.interfaces;

import com.openbravo.pos.payment.ccv.service.SocketMessage;

public interface IDeviceComm {
    void messageReceived(SocketMessage message);
}
