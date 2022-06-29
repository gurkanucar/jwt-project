package com.gucardev.jwtproject.service;

import com.gucardev.jwtproject.exception.GeneralException;
import com.gucardev.jwtproject.model.qrLogin.LoginQRCode;
import com.gucardev.jwtproject.model.qrLogin.LoginQRCodeType;
import com.gucardev.jwtproject.repository.LoginQRCodeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class LoginRequestService {

    private final LoginQRCodeRepository repository;

    private final SimpMessagingTemplate messagingTemplate;

    public LoginRequestService(LoginQRCodeRepository repository,
                               SimpMessagingTemplate messagingTemplate) {
        this.repository = repository;
        this.messagingTemplate = messagingTemplate;
    }

    public LoginQRCode save(LoginQRCode loginQRCode) {
        Optional<LoginQRCode> optionalObject = convertIteratorToList(repository.findAll()).stream().
                filter(p -> p.getRoom().equals(loginQRCode.getRoom())).
                findFirst();

        if (optionalObject.isPresent()) {
            var obj = optionalObject.get();
            log.info(String.format("updated => old: %s  new: %s", obj.getCode(), loginQRCode.getCode()));
            obj.setCode(loginQRCode.getCode());
            return repository.save(obj);
        }
        return repository.save(loginQRCode);
    }


    public void processMessage(String room, LoginQRCode loginQRCode, String sessionID) {

        var response = save(loginQRCode);
        //  log.info(String.format("%s", response.toString()));
    }

    public void sendMessage(LoginQRCode loginQRCode) {
        messagingTemplate.convertAndSend("/topic/fileStatus/" + loginQRCode.getRoom(), loginQRCode);
    }

    private List<LoginQRCode> convertIteratorToList(Iterable<LoginQRCode> iterator) {
        List<LoginQRCode> list = new ArrayList<>();
        iterator.iterator().forEachRemaining(list::add);
        return list;
    }
}
