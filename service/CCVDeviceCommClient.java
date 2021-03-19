package com.openbravo.pos.payment.ccv.service;

import com.openbravo.pos.payment.ccv.interfaces.IDeviceComm;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * The first TCP connection (Channel 0), connecting by POS side, listening from Payment Terminal side, will be used for
 * the ‘CardServiceRequest’ / ‘CardServiceResponse’ sessions and ‘ServiceRequest’ / ‘ServiceResponse’
 * sessions from the POS to the Payment Terminal;
 */
@Slf4j
public class CCVDeviceCommClient implements Runnable {
    private final List<IDeviceComm> listeners = new LinkedList<>();
    private final String paymentDeviceIpAddress;
    private final int devicePort;
    private SocketMessage outgoingMessage;

    public CCVDeviceCommClient(String PaymentDeviceIpAddress, int devicePort) {
        paymentDeviceIpAddress = PaymentDeviceIpAddress;
        this.devicePort = devicePort;
        log.info("Client init");
    }

    @Override
    public void run() {
        try {
            Selector selector = Selector.open();
            log.info("client selector is open");

            SocketChannel client = SocketChannel.open();
            log.info("client socket is open");
            client.configureBlocking(false);

            client.connect(new InetSocketAddress(paymentDeviceIpAddress, devicePort));
            client.register(selector, SelectionKey.OP_CONNECT);

            while (!Thread.currentThread().isInterrupted()) {

                if (!client.isConnected() && outgoingMessage != null){
                    client.connect(new InetSocketAddress(paymentDeviceIpAddress, devicePort));
                    client.register(selector, SelectionKey.OP_CONNECT);
                }

                selector.select();
                Set<SelectionKey> readyKeys = selector.selectedKeys();

                // process each ready key...
                Iterator<SelectionKey> iterator = readyKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();

                    if (key.isConnectable()) {
                        log.info("client Connectable");
                        client.finishConnect();
                        log.info(Thread.currentThread().getName() + " connected to device");
                        // prepare for read,
                        client.register(selector, SelectionKey.OP_WRITE);
                    } else if (key.isReadable()) {
                        log.info("client Readable");
                        // read
                        ByteBuffer inBuf = ByteBuffer.allocate(2048);
                        messageReceivedNotification(SocketMessage.nextMessageFromSocket((SocketChannel) key.channel(), inBuf));
                        // prepare for write,
                        client.register(selector, SelectionKey.OP_WRITE);
                    } else if (key.isWritable()) {
                        if (outgoingMessage != null) {
                            log.info("client Outgoing Message is exist");

                            SocketMessage.sendMessage((SocketChannel) key.channel(), outgoingMessage);
                            outgoingMessage = null;

                            // switch to read, and disable write,
                            //when we sent start listen
                            client.register(selector, SelectionKey.OP_READ);
                        }
                    }
                }
            }
        } catch (IOException e) {
            log.error("Error", e);
        }
    }

    public void sendMessageToDevice(SocketMessage socketMessage){
        outgoingMessage = socketMessage;
    }

    public void addListener(IDeviceComm observer) {
        listeners.add(observer);
    }

    private void messageReceivedNotification(SocketMessage socketMessage) {
        for (IDeviceComm listener : listeners) {
            listener.messageReceived(socketMessage);
        }
    }
}
