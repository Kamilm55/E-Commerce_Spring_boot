//package com.example.kamil.user.service.messaging;
//
//import com.example.kamil.user.model.entity.VendorRequest;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//@Slf4j
//@RequiredArgsConstructor
//public class KafkaConsumerServices {
//    private static final String TOPIC = "public-chats"; // Defines the Kafka topic from which messages will be consumed.
//    private final SimpMessagingTemplate messagingTemplate; //  Provided by Spring, which allows sending messages to WebSocket destinations
//    private final List<VendorRequest> chatMessages = new ArrayList<>();
//
//
//    @KafkaListener(topics = TOPIC, groupId = "my-group")
//    public void handleMessage(VendorRequest message) {
//        log.info("Received message from Kafka: {}", message);
//        chatMessages.add(message);
//        messagingTemplate.convertAndSend("/topic/public", message); // This is related to websocket not kafka
//        // Sends the message to the "/topic/public" WebSocket destination
//    }
//    public List<VendorRequest> getChatMessages() {
//        return new ArrayList<>(chatMessages);
//    }
//}
