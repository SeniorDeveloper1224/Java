/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.payment.ccv.service;

import com.openbravo.pos.payment.ccv.models.CardServiceResponse;
import com.openbravo.pos.payment.ccv.models.DeviceRequest;
import jdk.internal.org.xml.sax.InputSource;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Future;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Message is the base class of all messages to be send or received over the socket.
 * In this class we present the full set of functions needed to send and receive data
 * over a socket. In a real world case, we'd probably want better separation of concerns
 * but this is more than satisfactory for an example.
 *
 * Message has two abstract methods that must be implemented, these two methods handle
 * conversion to and from a byte buffer.
 */
@Slf4j
public abstract class SocketMessage {
    /**
     * Reads a single message from the socket, returning it as a sub class of Message
     * @param socket socket to read from
     * @param dataBuffer the data buffer to use
     * @return a message if it could be parsed
     * @throws IOException if the message could not be converted.
     */
    public static SocketMessage nextMessageFromSocket(SocketChannel socket, ByteBuffer dataBuffer) throws IOException {
        // read the first 4 bytes to get the message length.
        ensureBytesAvailable(socket, dataBuffer, 4);
        int length = dataBuffer.getInt();
        // read the rest of the message (as denoted by length)
        ensureBytesAvailable(socket, dataBuffer, length);
        String socketRawMessage = stringFromMsg(dataBuffer).substring(0, length);
        //message available as string
        log.info("Message Received and unmarshalling now : " + socketRawMessage);
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(socketRawMessage.getBytes()));
            JAXBContext jaxbContext;
            Unmarshaller jaxUnmarshall;
            switch (doc.getDocumentElement().getNodeName()) {
                case "DeviceRequest":
                    jaxbContext = JAXBContext.newInstance(DeviceRequest.class);
                    jaxUnmarshall = jaxbContext.createUnmarshaller();
                    jaxUnmarshall.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());
                    return (DeviceRequest) jaxUnmarshall.unmarshal(new StringReader(socketRawMessage));
                case "CardServiceResponse":
                    jaxbContext = JAXBContext.newInstance(CardServiceResponse.class);
                    jaxUnmarshall = jaxbContext.createUnmarshaller();
                    jaxUnmarshall.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());
                    return (CardServiceResponse) jaxUnmarshall.unmarshal(new StringReader(socketRawMessage));
                default:
                break;
            }


        } catch (JAXBException | ParserConfigurationException | SAXException ex) {
            log.error("Error", ex);
        }
        return null;        
    }

    /**
     * Send any message derived from Message base class on the socket,
     * @param channel the channel on which the message is sent
     * @param SocketMessageToSend message parameter to send
     * @throws IOException if there is a problem during writing.
     */
    public static void sendMessage(SocketChannel channel, SocketMessage SocketMessageToSend) throws IOException {
        JAXBContext jaxbContext;
        try {
            jaxbContext = JAXBContext.newInstance(SocketMessageToSend.getClass());
            Marshaller marshaller = jaxbContext.createMarshaller();

            marshaller.setProperty("com.sun.xml.internal.bind.xmlHeaders", "<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            marshaller.setProperty("com.sun.xml.internal.bind.xmlDeclaration", Boolean.FALSE);
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,Boolean.TRUE);

            final ByteArrayOutputStream msgBytes  = new ByteArrayOutputStream();
            marshaller.marshal(SocketMessageToSend,msgBytes);
            marshaller.marshal(SocketMessageToSend,System.out);

            ByteBuffer bbMsg = ByteBuffer.allocate(msgBytes.size() + 4);
            bbMsg.put(intToByteArray(msgBytes.size()));
            bbMsg.put(msgBytes.toByteArray());
            bbMsg.flip();
            while(bbMsg.hasRemaining()) {
                channel.write(bbMsg);
            }
        } catch (JAXBException ex) {
            log.error("Error", ex);
        }
    }
    /**
     * Must be implemented by all sub classes to convert the message into
     * bytes in the buffer.
     * @param value the byte buffer to receive the message data.
     */
    public static byte[] intToByteArray(int value) {
        return new byte[] {
                (byte)(value >>> 24),
                (byte)(value >>> 16),
                (byte)(value >>> 8),
                (byte)value};
    }

    /**
     * Converts a string into a message field in the buffer passed in.
     * into the buffer
     * @param buffer the buffer that represents the socket
     * @param str the string to be written
     */
    public static void stringToMsg(ByteBuffer buffer, String str) {
        byte[] bytes = str.getBytes();
        buffer.put(intToByteArray(bytes.length));
        buffer.put(bytes);
    }

    /**
     * converts a message field from the buffer into a string
     * @param buffer the message as a buffer
     * @return the string field
     */
    public static String stringFromMsg(ByteBuffer buffer) {
        return new String(buffer.array());
    }
    /**
     * When we are reading messages from the wire, we need to ensure there are
     * enough bytes in the buffer to fully decode the message. If not we keep
     * reading until we have enough.
     * @param socket the socket to read from
     * @param buffer the buffer to store the bytes
     * @param required the amount of data required.
     * @throws IOException if the socket closes or errors out.
     */
    private static void ensureBytesAvailable(SocketChannel socket, ByteBuffer buffer, int required) throws IOException {
        // if there's already something in the buffer, then compact it and prepare it for writing again.
        if(buffer.position() != 0) {
            buffer.compact();
        }

        // we loop until we have enough data to decode the message
        while(buffer.position() < required) {
            // try and read, if read returns 0 or less, the socket's closed.
            int len = socket.read(buffer);

            /*
            if(!socket.isOpen() || len <= 0) {
                throw new IOException("Socket closed while reading");
            }
             */
        }
        // and finally, prepare the buffer for reading.
        buffer.flip();
    }
}