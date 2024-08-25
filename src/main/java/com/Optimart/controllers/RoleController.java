package com.Optimart.controllers;

import com.Optimart.constants.Endpoint;
import com.Optimart.dto.Role.CreateRole;
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
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Object.class), mediaType = "application/json"))
    @GetMapping()
    public ResponseEntity<APIResponse<RoleResponse>> getRoles (
                         @RequestParam(defaultValue = "10") int limit,
                         @RequestParam(defaultValue = "1") int page,
                         @RequestParam(required = false) String search,
                         @RequestParam(defaultValue = "createdAt asc") String order) {
        return ResponseEntity.ok().body(roleService.getRoles(limit,page,search,order));
    }

    @ApiResponse(responseCode = "201", description = "SUCCESS OPERATION", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @PostMapping()
    public ResponseEntity<BaseResponse> addRole(@RequestBody CreateRole createRole) {
        String name = createRole.getName();
        return ResponseEntity.ok(new BaseResponse(LocalDate.now(), roleService.addRole(name)));
    }
}
