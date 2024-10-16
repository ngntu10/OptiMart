package com.Optimart.controllers;

import com.Optimart.annotations.SecuredSwaggerOperation;
import com.Optimart.constants.Endpoint;
import com.Optimart.dto.Order.CreateOrderDTO;
import com.Optimart.models.Order;
import com.Optimart.responses.APIResponse;
import com.Optimart.responses.Order.OrderResponse;
import com.Optimart.responses.User.UserResponse;
import com.Optimart.services.Order.OrderService;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@Tag(name = "Orders", description = "Everything about orders")
@RequestMapping(Endpoint.Order.BASE)
public class OrderController {

    public final OrderService orderService;

    @ApiResponse(responseCode = "201", description = "CREATED", content = @Content(schema = @Schema(implementation = Object.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Create a new order")
    @PostMapping
    public ResponseEntity<APIResponse<OrderResponse>> createOrder(@RequestBody CreateOrderDTO createOrderDTO){
        return ResponseEntity.ok(orderService.createOrder(createOrderDTO));
    }

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Object.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Get all order by user")
    @GetMapping(Endpoint.Order.ME)
    public ResponseEntity<?> getAllOrderUser(@RequestParam Map<Object, String> filters, @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(orderService.getAllOrderByMe(filters, token));
    }

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Object.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Get all order by user")
    @PostMapping(Endpoint.Order.CANCEL)
    public ResponseEntity<?> cancelOrder(@PathVariable String orderId){
        return ResponseEntity.ok(orderService.cancelOrder(orderId));
    }
}
