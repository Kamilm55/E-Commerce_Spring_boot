package com.example.kamil.user.service.sse;

import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class SseService {

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public void addEmitter(SseEmitter emitter) {
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
    }

    // Method to send a message to all connected clients
    public void sendMessageToClients(String message) {
        List<SseEmitter> deadEmitters = new ArrayList<>();
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(message, MediaType.TEXT_PLAIN);
            } catch (IOException e) {
                emitter.completeWithError(e);
                deadEmitters.add(emitter);
            }
        }
        // Remove dead emitters
        emitters.removeAll(deadEmitters);
    }
    @Scheduled(fixedRate = 1000)
    public void sendEvents() {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(System.currentTimeMillis());
            } catch (IOException e) {
                emitter.complete();
                emitters.remove(emitter);
            }
        }
    }
}
