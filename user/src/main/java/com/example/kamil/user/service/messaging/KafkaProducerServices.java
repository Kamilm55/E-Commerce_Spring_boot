//package com.example.kamil.user.service.messaging;
//
//import com.example.kamil.user.model.entity.VendorRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor // dependency injection with constructor
//public class KafkaProducerServices {
//    private static final String TOPIC = "public-chats";
//    private final KafkaTemplate<String, VendorRequest> kafkaTemplate;
//
//    public void sendMessage(VendorRequest message) {
//        kafkaTemplate.send(TOPIC, message);
//    }
//}
