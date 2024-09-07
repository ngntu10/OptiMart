package com.Optimart.controllers;

import com.Optimart.annotations.SecuredSwaggerOperation;
import com.Optimart.constants.Endpoint;
import com.Optimart.responses.APIResponse;
import com.Optimart.responses.User.UserResponse;
import com.Optimart.services.User.UserService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<APIResponse<?> > getAllUser(
             @RequestParam(defaultValue = "10") int limit,
             @RequestParam(defaultValue = "1") int page,
             @RequestParam(required = false) String search,
             @RequestParam(defaultValue = "createdAt asc") String order,
             @RequestParam(defaultValue = "") String roleId,
             @RequestParam(defaultValue = "1") int status,
             @RequestParam(defaultValue = "") String cityId,
             @RequestParam(defaultValue = "") int userType
    ) {
         return ResponseEntity.ok(userService.getUsers(limit, page, search, order, roleId, status, cityId, userType));
    }

    @SecuredSwaggerOperation(summary = "Update an existing user")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Object.class), mediaType = "application/json"))
    @GetMapping(Endpoint.User.ID)
    public ResponseEntity<?> getOneUser(@RequestParam UUID userId){
        return null;
    }
}
