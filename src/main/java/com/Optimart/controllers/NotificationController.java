package com.Optimart.controllers;

import com.Optimart.annotations.SecuredSwaggerOperation;
import com.Optimart.constants.Endpoint;
import com.Optimart.constants.MessageKeys;
import com.Optimart.dto.FirebaseCloud.Note;
import com.Optimart.exceptions.DataNotFoundException;
import com.Optimart.models.DeliveryType;
import com.Optimart.models.Notification;
import com.Optimart.models.User;
import com.Optimart.repositories.AuthRepository;
import com.Optimart.responses.APIResponse;
import com.Optimart.responses.PagingResponse;
import com.Optimart.services.Firebase.FirebaseMessagingService;
import com.Optimart.services.Notification.NotificationService;
import com.Optimart.utils.JwtTokenUtil;
import com.Optimart.utils.LocalizationUtils;
import com.google.firebase.messaging.FirebaseMessagingException;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequestMapping(Endpoint.Notification.BASE)
@RequiredArgsConstructor
@RestController
@Tag(name = "Notification", description = "Everything about send notification")
public class NotificationController {
    private final FirebaseMessagingService firebaseMessagingService;
    private final NotificationService notificationService;
    private final JwtTokenUtil jwtTokenUtil;
    private final LocalizationUtils localizationUtils;
    private final AuthRepository authRepository;

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Object.class)), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Send notification to cloud")
    @PostMapping
    public ResponseEntity<?> sendNotification(@RequestBody Note note,
                                           @RequestParam String token) throws FirebaseMessagingException {
        try {
            return ResponseEntity.ok(firebaseMessagingService.sendNotification(note, token));
        } catch (Exception ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Object.class)), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Get list all notification with filters")
    @GetMapping
    public ResponseEntity<?> getAllNotification(@RequestParam Map<Object, String> filters){
        try {
            PagingResponse<List<Notification>> items = notificationService.getNotifications(filters);
            return ResponseEntity.ok(items);
        } catch(Exception ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Object.class)), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Mark read notification")
    @PostMapping(Endpoint.Notification.MARK_READ)
    public ResponseEntity<?> markReadNotification(@PathVariable String notificationId ){
        APIResponse<Notification> notificationAPIResponse = notificationService.markReadNotification(notificationId);
        return ResponseEntity.ok(notificationAPIResponse);
    }

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Object.class)), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "User delete notification")
    @DeleteMapping(Endpoint.Notification.DELETE_NOTIFICATION)
    public ResponseEntity<?> deleteNotification(@PathVariable String notificationId ){
        APIResponse<Boolean> notificationAPIResponse = notificationService.deleteNotification(notificationId);
        return ResponseEntity.ok(notificationAPIResponse);
    }

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Object.class)), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "User read all notifications")
    @PostMapping(Endpoint.Notification.MARK_READ_ALL_NOTIFICATION)
    public ResponseEntity<?> readAllNotification(@RequestHeader("Authorization") String token){
        APIResponse<Boolean> notificationAPIResponse = notificationService.readAllNotification(token);
        return ResponseEntity.ok(notificationAPIResponse);
    }

    private User getUser(String token){
        String jwtToken = token.substring(7);
        String email = jwtTokenUtil.extractEmail(jwtToken);
        Optional<User> optionalUser = authRepository.findByEmail(email);
        if(optionalUser.isEmpty())
            throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.USER_NOT_EXIST));
        return optionalUser.get();
    }
}

