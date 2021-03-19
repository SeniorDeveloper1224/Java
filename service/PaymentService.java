package com.openbravo.pos.payment.ccv.service;

import com.openbravo.pos.payment.ccv.enums.EnumCardServiceRequestType;
import com.openbravo.pos.payment.ccv.enums.EnumOutResult;
import com.openbravo.pos.payment.ccv.enums.EnumOutputTarget;
import com.openbravo.pos.payment.ccv.enums.EnumOverallResult;
import com.openbravo.pos.payment.ccv.models.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PaymentService {
    CCVDeviceCommServer serverService;
    CCVDeviceCommClient clientService;
    //
    String WorkstationId = "POS";
    String LanguageCode = "nl";
    String PrinterStatus = "Available";
    String eJournalStatus = "Available";
    String JournalPrinterStatus = "Available";
    int ShiftNumber = 1;
    int ClerkID = 1;

    /**
     * @param PaymentDeviceIpAddress
     * @param devicePort
     * @param serverPort
     */
    public PaymentService(String PaymentDeviceIpAddress, int devicePort, int serverPort) {

        //start to listen first
        serverService = new CCVDeviceCommServer(serverPort);
        serverService.addListener(socketMessage -> {
            if (socketMessage instanceof DeviceRequest){
                DeviceRequest deviceRequest = (DeviceRequest)socketMessage;
                switch (deviceRequest.Output.OutDeviceTarget){
                    case CashierDisplay:
                        log.info("CashierDisplay request");
                        //responding with success
                        sendServerDeviceResponse(deviceRequest);
                        break;
                    case Printer:
                        log.info("Printer request");
                        break;
                    case JournalPrinter:
                        log.info("JournalPrinter request");
                        break;
                    case EJournal:
                        log.info("EJournal request");
                        break;
                    default:
                }
            }
        });

        new Thread(serverService).start();




        //communicate with device second
        clientService = new CCVDeviceCommClient(PaymentDeviceIpAddress, devicePort);
        clientService.addListener(socketMessage -> {
            if (socketMessage instanceof CardServiceResponse){
                CardServiceResponse incomingCardServiceResponse = (CardServiceResponse)socketMessage;
                switch (incomingCardServiceResponse.RequestType){
                    case CardPayment:
                        log.info("CardPayment request");
                        if (incomingCardServiceResponse.OverallResult == EnumOverallResult.PrintLastTicket){
                            log.info("Sending Reprint request");
                            sendServerReprint();
                        }

                        break;
                    case Output:
                        log.info("Output request");
                        break;
                    case TicketReprint:
                        log.info("TicketReprint request");
                        break;
                    default:
                }
            }
        });
        new Thread(clientService).start();

        initializePayment(3.55);
    }

    private void sendServerReprint(){
        int RequestId = 2;

        CardServiceRequest reprintMessage = new CardServiceRequest(WorkstationId, RequestId, EnumCardServiceRequestType.TicketReprint,
                new POSdata(LanguageCode,ShiftNumber, ClerkID, PrinterStatus, eJournalStatus, JournalPrinterStatus),
                null
        );

        serverService.setOutgoingMessage(reprintMessage);
    }

    private void sendServerDeviceResponse(DeviceRequest sourceSocketMessage){
        serverService.setOutgoingMessage(
                new DeviceResponse(WorkstationId,sourceSocketMessage.RequestID,sourceSocketMessage.RequestType,
                        new Output(sourceSocketMessage.Output.OutDeviceTarget, EnumOutResult.Success), EnumOverallResult.Success));
    }

    private void initializePayment(double Amount) {

        int RequestId = 2;



        CardServiceRequest initPaymentCardServiceRequest = new CardServiceRequest(WorkstationId, RequestId, EnumCardServiceRequestType.CardPayment,
                new POSdata(LanguageCode,ShiftNumber, ClerkID, PrinterStatus, eJournalStatus, JournalPrinterStatus),
                new TotalAmount(Amount)
        );

        clientService.sendMessageToDevice(initPaymentCardServiceRequest);
    }
}
