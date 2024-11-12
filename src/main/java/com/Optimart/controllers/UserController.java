package com.Optimart.controllers;

import com.Optimart.annotations.SecuredSwaggerOperation;
import com.Optimart.constants.Endpoint;
import com.Optimart.dto.User.CreateUserDTO;
import com.Optimart.dto.User.EditUserDTO;
import com.Optimart.dto.User.UserMutilDeleteDTO;
import com.Optimart.dto.User.UserSearchDTO;
import com.Optimart.models.Paymenttype;
import com.Optimart.models.User;
import com.Optimart.responses.APIResponse;
import com.Optimart.responses.PagingResponse;
import com.Optimart.responses.User.UserResponse;
import com.Optimart.services.User.UserService;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Tag(name = "User", description = "Everything about user")
@RequestMapping(Endpoint.User.BASE)
public class UserController {
    public final UserService userService;

    @PreAuthorize("hasAuthority('SYSTEM.USER.VIEW') OR hasAuthority('ADMIN.GRANTED')")
    @SecuredSwaggerOperation(summary = "Get all user")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Paymenttype.class)), mediaType = "application/json"))
    @GetMapping
    public ResponseEntity<PagingResponse<?>> getAllUser(@ModelAttribute UserSearchDTO userSearchDTO) {
         return ResponseEntity.ok(userService.getUsers(userSearchDTO));
    }

    @SecuredSwaggerOperation(summary = "Get details for an existing user")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = UserResponse.class), mediaType = "application/json"))
    @GetMapping(Endpoint.User.ID)
    public ResponseEntity<UserResponse> getOneUser(@PathVariable String userId){
        return ResponseEntity.ok(userService.getOneUser(userId));
    }

    @PreAuthorize("hasAuthority('SYSTEM.USER.CREATE') OR hasAuthority('ADMIN.GRANTED')")
    @SecuredSwaggerOperation(summary = "Create a new user")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Object.class), mediaType = "application/json"))
    @PostMapping
    public ResponseEntity<APIResponse<User>> createUser(@RequestBody CreateUserDTO createUserDTO){
        return ResponseEntity.ok(userService.createNewUser(createUserDTO));
    }


    @SecuredSwaggerOperation(summary = "Update an existing user")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Object.class), mediaType = "application/json"))
    @PutMapping(Endpoint.User.ID)
    public ResponseEntity<APIResponse<UserResponse>> editUser(@RequestBody EditUserDTO user){
        return ResponseEntity.ok(userService.editUser(user));
    }

    @PreAuthorize("hasAuthority('SYSTEM.USER.DELETE') OR hasAuthority('ADMIN.GRANTED')")
    @SecuredSwaggerOperation(summary = "Delete an existing user")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Object.class), mediaType = "application/json"))
    @DeleteMapping(Endpoint.User.ID)
    public ResponseEntity<APIResponse<Boolean>> deleteUser(@PathVariable String userId){
        return ResponseEntity.ok(userService.deleteUser(userId));
    }

    @PreAuthorize("hasAuthority('SYSTEM.USER.DELETE') OR hasAuthority('ADMIN.GRANTED')")
    @SecuredSwaggerOperation(summary = "Delete multi user")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Object.class), mediaType = "application/json"))
    @DeleteMapping(Endpoint.User.DELETE_MANY)
    public ResponseEntity<APIResponse<Boolean>> deleteMutiUser(@RequestBody UserMutilDeleteDTO userMutilDeleteDTO){
        return ResponseEntity.ok(userService.deleteMutilUser(userMutilDeleteDTO));
    }

}
