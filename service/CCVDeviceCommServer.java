package com.openbravo.pos.payment.ccv.service;

import com.openbravo.pos.payment.ccv.interfaces.IDeviceComm;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * The second TCP connection (Channel 1), listening by POS side, connecting from Payment Terminal side,
 * will be used for the ‘DeviceRequest’ / ’DeviceResponse’ sessions from the Payment Terminal to the POS.
 */
@Slf4j
public class CCVDeviceCommServer implements Runnable {
    private final int serverPort;
    private final List<IDeviceComm> listeners = new LinkedList<>();
    private SocketMessage outgoingMessage;
    public CCVDeviceCommServer(int serverPort) {
        this.serverPort = serverPort;
    }

    @Override
    public void run() {
        try {
            ServerSocketChannel server = ServerSocketChannel.open();
            server.socket().bind(new InetSocketAddress("192.168.178.200", serverPort));
            server.configureBlocking(false);

            log.info("Server binded");

            Selector selector=Selector.open();
            server.register(selector, SelectionKey.OP_ACCEPT);

            while(!Thread.currentThread().isInterrupted()){
                if (!server.socket().isBound() && outgoingMessage != null){
                    log.info("Binding server again.");
                    server.socket().bind(new InetSocketAddress("192.168.178.200", serverPort));
                }

                selector.select();
                Set<SelectionKey> readyKeys=selector.selectedKeys();

                // process each ready key...
                Iterator<SelectionKey> iterator=readyKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key= iterator.next();
                    iterator.remove();

                    if (key.isAcceptable()) {
                        log.info("Server Accepting");
                        SocketChannel client= server.accept();
                        log.info(Thread.currentThread().getName() + " accept connection");
                        client.configureBlocking(false);

                        // prepare for read,
                        client.register(selector, SelectionKey.OP_READ);
                    } else if (key.isReadable()) {
                        log.info("Server Readable");
                        // read
                        SocketChannel client=(SocketChannel) key.channel();
                        ByteBuffer inBuf=ByteBuffer.allocate(2048);
                        messageReceivedNotification(SocketMessage.nextMessageFromSocket(client,inBuf));

                        // prepare for write,
                        client.register(selector, SelectionKey.OP_WRITE);
                    } else if (key.isWritable()) {
                        SocketChannel client=(SocketChannel) key.channel();
                        if(outgoingMessage != null){
                            log.info("Server outgoing message");
                            SocketMessage.sendMessage(client,outgoingMessage);
                            outgoingMessage = null;
                            // switch to read, and disable write,
                            client.register(selector, SelectionKey.OP_READ);
                        }
                    }
                }
            }
        } catch (IOException e) {
            log.error("Error", e);
        }
    }

    public void setOutgoingMessage(SocketMessage message){
        outgoingMessage = message;
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
