package com.example.kamil.user.controller;

import com.example.kamil.user.service.messaging.MessageSenderServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/message")
@RequiredArgsConstructor
public class MessageController {

    private final MessageSenderServiceImpl messageSenderService;

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody String message) {
        // complete this
        //TODO: https://alexandernikolov.tech/implement-websocket-communication-with-rabbitmq#heading-what-are-websockets
        messageSenderService.sendMessage("amq.topic", "messages", message);
        return ResponseEntity.ok("Message sent: " + message);
    }
}