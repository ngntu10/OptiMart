package com.Optimart.controllers;

import com.Optimart.annotations.SecuredSwaggerOperation;
import com.Optimart.constants.Endpoint;
import com.Optimart.dto.User.UserSearchDTO;
import com.Optimart.responses.APIResponse;
import com.Optimart.responses.User.PagingUserResponse;
import com.Optimart.responses.User.UserResponse;
import com.Optimart.services.User.UserService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@Tag(name = "User", description = "Everything about user")
@RequestMapping(Endpoint.User.BASE)
public class UserController {
    public final UserService userService;
    @SecuredSwaggerOperation(summary = "Get all user")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Object.class), mediaType = "application/json"))
    @GetMapping
    public ResponseEntity<PagingUserResponse<List<UserResponse>> > getAllUser(@ModelAttribute UserSearchDTO userSearchDTO) {
         return ResponseEntity.ok(userService.getUsers(userSearchDTO));
    }

    @SecuredSwaggerOperation(summary = "Update an existing user")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Object.class), mediaType = "application/json"))
    @GetMapping(Endpoint.User.ID)
    public ResponseEntity<?> getOneUser(@RequestParam UUID userId){
        return null;
    }
}
