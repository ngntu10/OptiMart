package com.Optimart.controllers;

import com.Optimart.annotations.SecuredSwaggerOperation;
import com.Optimart.constants.Endpoint;
import com.Optimart.dto.Role.CreateRole;
import com.Optimart.models.Role;
import com.Optimart.responses.APIResponse;
import com.Optimart.responses.BaseResponse;
import com.Optimart.responses.Role.RoleResponse;
import com.Optimart.services.Role.RoleService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
@Tag(name = "Role", description = "Everything about role")
@RequestMapping(Endpoint.Role.Base)
public class RoleController {

    private final RoleService roleService;

    @SecuredSwaggerOperation(summary = "Get all role")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Object.class), mediaType = "application/json"))
    @GetMapping
    public ResponseEntity<APIResponse<RoleResponse>> getRoles (
                         @RequestParam(defaultValue = "10") int limit,
                         @RequestParam(defaultValue = "1") int page,
                         @RequestParam(required = false) String search,
                         @RequestParam(defaultValue = "createdAt asc") String order) {
        return ResponseEntity.ok().body(roleService.getRoles(limit,page,search,order));
    }

    @SecuredSwaggerOperation(summary = "Add a new role")
    @PostMapping
    public ResponseEntity<BaseResponse> addRole(@RequestBody CreateRole createRole) {
        String name = createRole.getName();
        return ResponseEntity.ok(new BaseResponse(LocalDate.now(), roleService.addRole(name)));
    }

    @SecuredSwaggerOperation(summary = "Get details roles by ID")
    @GetMapping("/{roleId}")
    public ResponseEntity<Role> getOne(@PathVariable String roleId){
        return ResponseEntity.ok().body(roleService.getOne(roleId));
    }

    @SecuredSwaggerOperation(summary = "Update an existing role")
    @PatchMapping("/{roleId}")
    public ResponseEntity<?> editRole(@PathVariable String roleId, @RequestBody String name){
        return ResponseEntity.ok().body(roleService.editRole(roleId, name));
    }

    @SecuredSwaggerOperation(summary = "Delete role by ID")
    @DeleteMapping("/{roleId}")
    public ResponseEntity<?> deleteRole(@PathVariable String roleId){
        return ResponseEntity.ok().body(roleService.deleteRole(roleId));
    }
}
