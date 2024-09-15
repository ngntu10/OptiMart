package com.Optimart.controllers;

import com.Optimart.annotations.SecuredSwaggerOperation;
import com.Optimart.constants.Endpoint;
import com.Optimart.dto.User.CreateUserDTO;
import com.Optimart.dto.User.EditUserDTO;
import com.Optimart.dto.User.UserSearchDTO;
import com.Optimart.models.User;
import com.Optimart.responses.APIResponse;
import com.Optimart.responses.User.PagingUserResponse;
import com.Optimart.responses.User.UserResponse;
import com.Optimart.services.User.UserService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Tag(name = "User", description = "Everything about user")
@RequestMapping(Endpoint.User.BASE)
public class UserController {
    public final UserService userService;
    @SecuredSwaggerOperation(summary = "Get all user")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Object.class), mediaType = "application/json"))
    @GetMapping
//    @PreAuthorize("hasAnyAuthority('ROLE_SYSTEM.USER.VIEW')")
    public ResponseEntity<PagingUserResponse<?>> getAllUser(@ModelAttribute UserSearchDTO userSearchDTO) {
         return ResponseEntity.ok(userService.getUsers(userSearchDTO));
    }

    @SecuredSwaggerOperation(summary = "Get details for an existing user")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Object.class), mediaType = "application/json"))
    @GetMapping(Endpoint.User.ID)
//    @PreAuthorize("hasAuthority('ROLE_SYSTEM.USER.VIEW')")
    public ResponseEntity<UserResponse> getOneUser(@PathVariable String userId){
        return ResponseEntity.ok(userService.getOneUser(userId));
    }

    @SecuredSwaggerOperation(summary = "Create a new user")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Object.class), mediaType = "application/json"))
    @PostMapping
    public ResponseEntity<APIResponse<User>> createUser(@RequestBody CreateUserDTO createUserDTO){
        return ResponseEntity.ok(userService.createNewUser(createUserDTO));
    }
    @SecuredSwaggerOperation(summary = "Update an existing user")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Object.class), mediaType = "application/json"))
    @PatchMapping(Endpoint.User.ID)
    public ResponseEntity<APIResponse<UserResponse>> editUser(@RequestBody EditUserDTO user){
        return ResponseEntity.ok(userService.editUser(user));
    }

    @SecuredSwaggerOperation(summary = "Delete an existing user")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Object.class), mediaType = "application/json"))
    @DeleteMapping(Endpoint.User.ID)
    public ResponseEntity<APIResponse<Boolean>> deleteUser(@PathVariable String userId){
        return ResponseEntity.ok(userService.deleteUser(userId));
    }

    @SecuredSwaggerOperation(summary = "Delete an existing user")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Object.class), mediaType = "application/json"))
    @DeleteMapping(Endpoint.User.DELETE_MANY)
    public ResponseEntity<APIResponse<String>> deleteMutiUser(@RequestBody List<String> userIdList){
        return null;
    }

}
