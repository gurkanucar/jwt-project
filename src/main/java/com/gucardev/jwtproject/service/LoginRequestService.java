package com.gucardev.jwtproject.service;

import com.gucardev.jwtproject.exception.GeneralException;
import com.gucardev.jwtproject.model.LoginQRCode;
import com.gucardev.jwtproject.model.LoginQRCodeType;
import com.gucardev.jwtproject.repository.LoginQRCodeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

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


//    public boolean isValid(LoginQRCode loginQRCode) {
//        var existing = getByRoom(loginQRCode.getRoom());
//        if (!existing.getCode().equals(loginQRCode.getCode())) {
//            throw new GeneralException("Code is invalid!", HttpStatus.BAD_REQUEST);
//        }
//        return true;
//    }

    public LoginQRCode get(Long id) {
        return repository.findById(id).orElseThrow(() -> new GeneralException("Not Found!", 404));
    }

    public LoginQRCode update(LoginQRCode loginQRCode) {
        var existing = repository.findById(loginQRCode.getId()).orElseThrow(() -> new GeneralException("Not Found!", 404));
        existing.setCode(loginQRCode.getCode());
        existing.setConnectedUsers(loginQRCode.getConnectedUsers());
        return repository.save(existing);
    }

    public LoginQRCode create(LoginQRCode loginQRCode) {
        repository.findById(loginQRCode.getId()).ifPresent((s) -> {
            throw new GeneralException("Already Exists!", HttpStatus.CONFLICT);
        });
        return repository.save(loginQRCode);
    }

    public void delete(Long id) {
        var existing = repository.findById(id).orElseThrow(() -> new GeneralException("Not Found!", 404));
        repository.delete(existing);
    }


    public LoginQRCode getByRoom(String room) {
        return repository.findLoginQRCodeByRoom(room).orElseThrow(() -> new GeneralException("Not Found!", 404));
    }

    public LoginQRCode addToUsers(LoginQRCode entity, String userSessionID) {
        var existing = getByRoom(entity.getRoom());
        if (existing.getConnectedUsers().size() < 2) {
            if (!existing.getConnectedUsers().contains(userSessionID)) {
                existing.getConnectedUsers().add(userSessionID);
            }
            return update(entity);
        }
        throw new GeneralException("maximum number of users reached", 403);
    }

    public void processMessage(String room, LoginQRCode loginQRCode, String sessionID) {

        log.info(String.format("room: %s, room2: %s ,code: %s,type: %s, session: %s,",
                room, loginQRCode.getRoom(), loginQRCode.getCode(),
                loginQRCode.getType(), sessionID));

        if (!room.equals(loginQRCode.getRoom())) {
            throw new GeneralException("Room Not Found", HttpStatus.NOT_FOUND);
        }
        addToUsers(loginQRCode, sessionID);

        if (loginQRCode.getType().equals(LoginQRCodeType.MOBILE)) {
            // check for codes and room
            var existingRoom = getByRoom(loginQRCode.getRoom());
            if (!existingRoom.getCode().equals(loginQRCode.getCode())) {
                throw new GeneralException("Code is invalid", HttpStatus.NOT_FOUND);
            }
            //send jwt token to socket
            sendMessage(LoginQRCode.builder().message("JWT").type(LoginQRCodeType.SERVER).build());
        } else if (loginQRCode.getType().equals(LoginQRCodeType.CLIENT)) {
            update(loginQRCode);
        }

    }

    public void sendMessage(LoginQRCode loginQRCode) {
        messagingTemplate.convertAndSend("/topic/fileStatus/" + loginQRCode.getRoom(), loginQRCode);
    }
}
