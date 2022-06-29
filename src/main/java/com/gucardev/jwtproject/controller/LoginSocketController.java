package com.gucardev.jwtproject.controller;

import com.gucardev.jwtproject.model.qrLogin.LoginQRCode;
import com.gucardev.jwtproject.service.LoginRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class LoginSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final LoginRequestService service;


    public LoginSocketController(SimpMessagingTemplate messagingTemplate,
                                 LoginRequestService service) {
        this.messagingTemplate = messagingTemplate;
        this.service = service;
    }

    @MessageMapping("/login/{room}")
    public void messageHandler(@DestinationVariable String room,
                               @Payload LoginQRCode loginQRCode,
                               @Header("simpSessionId") String sessionId) {
        service.processMessage(room, loginQRCode, sessionId);
       // log.error("Status for: " + room);
        //messagingTemplate.convertAndSend("/topic/fileStatus/" + id, msg);

    }


}
