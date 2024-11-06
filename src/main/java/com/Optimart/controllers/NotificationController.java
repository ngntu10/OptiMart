package com.Optimart.controllers;

import com.Optimart.constants.Endpoint;
import com.Optimart.dto.FirebaseCloud.Note;
import com.Optimart.services.Firebase.FirebaseMessagingService;
import com.google.firebase.messaging.FirebaseMessagingException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(Endpoint.Notification.BASE)
@RequiredArgsConstructor
@RestController
@Tag(name = "Notification", description = "Everything about send notification")
public class NotificationController {
    private final FirebaseMessagingService firebaseMessagingService;

    @PostMapping
    public ResponseEntity<?> sendNotification(@RequestBody Note note,
                                           @RequestParam String token) throws FirebaseMessagingException {
        return ResponseEntity.ok(firebaseMessagingService.sendNotification(note, token));
    }
}

