package com.example.kamil.user.controller;

import com.example.kamil.user.model.entity.VendorRequest;
import com.example.kamil.user.service.messaging.KafkaConsumerServices;
import com.example.kamil.user.service.messaging.KafkaProducerServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class WebSocketController {
    private final KafkaProducerServices kafkaProducerServices;
    private final KafkaConsumerServices kafkaConsumerServices;
    public WebSocketController(KafkaProducerServices kafkaProducerServices, KafkaConsumerServices kafkaConsumerServices) {
        this.kafkaProducerServices = kafkaProducerServices;
        this.kafkaConsumerServices = kafkaConsumerServices;
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public void handleChatMessage(@Payload VendorRequest message) {
        // Send the message to Kafka
        System.out.println("isledi");
        kafkaProducerServices.sendMessage(message);
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public void addUser(@Payload VendorRequest message, SimpMessageHeaderAccessor headerAccessor){
        //add user to join room chat
        log.info("User added which is userDetails id is : {}", message.getUserDetails().getId());
        if (headerAccessor != null && headerAccessor.getSessionAttributes() != null) {
            headerAccessor.getSessionAttributes().put("userDetails_id",  message.getUserDetails().getId());
        } else {
            log.error("headerAccessor or session attributes is null.");
        }
        kafkaProducerServices.sendMessage(message);
    }
    @MessageMapping("/chat.removeUser")
    @SendTo("/topic/public")
    public void removeUser(@Payload VendorRequest message, SimpMessageHeaderAccessor headerAccessor) {
        log.info("User disconnected: {}", message.getUserDetails().getId());
        kafkaProducerServices.sendMessage(message);
        if (headerAccessor != null && headerAccessor.getSessionAttributes() != null) {
            headerAccessor.getSessionAttributes().remove("userDetails_id");
        } else {
            log.error("headerAccessor or session attributes is null.");
        }
    }
    @GetMapping("/api/chat")
    public List<VendorRequest> getChatMessages() {
        return kafkaConsumerServices.getChatMessages();
    }
}
