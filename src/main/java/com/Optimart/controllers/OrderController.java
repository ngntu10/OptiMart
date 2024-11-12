package com.Optimart.controllers;

import com.Optimart.annotations.SecuredSwaggerOperation;
import com.Optimart.constants.Endpoint;
import com.Optimart.dto.Order.ChangeOrderStatus;
import com.Optimart.dto.Order.CreateOrderDTO;
import com.Optimart.models.Order;
import com.Optimart.responses.APIResponse;
import com.Optimart.responses.Order.OrderResponse;
import com.Optimart.responses.PagingResponse;
import com.Optimart.responses.User.UserResponse;
import com.Optimart.services.Order.OrderService;
import com.google.firebase.messaging.FirebaseMessagingException;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderDTO createOrderDTO) throws FirebaseMessagingException {
        try{
            return ResponseEntity.ok(orderService.createOrder(createOrderDTO));
        } catch(Exception ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('MANAGE_ORDER.ORDER.VIEW') OR hasAuthority('ADMIN.GRANTED')")
    @ApiResponse(responseCode = "201", description = "CREATED", content = @Content(schema = @Schema(implementation = Object.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Get all orders")
    @GetMapping
    public ResponseEntity<?> getAllOrders(@RequestParam Map<Object, String> filters){
        return ResponseEntity.ok(orderService.getAllOrder(filters));
    }

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Object.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Get all order by user")
    @GetMapping(Endpoint.Order.ME)
    public ResponseEntity<PagingResponse<List<Order>>> getAllOrderUser(@RequestParam Map<Object, String> filters, @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(orderService.getAllOrderByMe(filters, token));
    }

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Object.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Cancel an order")
    @PostMapping(Endpoint.Order.CANCEL)
    public ResponseEntity<APIResponse<?>> cancelOrder(@PathVariable String orderId) {
        try {
            return ResponseEntity.ok(orderService.cancelOrder(orderId));
        }catch (Exception ex){
            return ResponseEntity.badRequest().body(new APIResponse<>(null, ex.getMessage()));
        }
    }

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Object.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Get an existing user's order by id")
    @GetMapping(Endpoint.Order.ID_ME)
    public ResponseEntity<APIResponse<Order>> getOrderMeById(@PathVariable String orderId){
        return ResponseEntity.ok(orderService.getOneOrderById(orderId));
    }

    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Object.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Get an existing order by id")
    @GetMapping(Endpoint.Order.ID)
    public ResponseEntity<APIResponse<Order>> getOrderById(@PathVariable String orderId){
        return ResponseEntity.ok(orderService.getOneOrderById(orderId));
    }


    @PreAuthorize("hasAuthority('MANAGE_ORDER.ORDER.DELETE') OR hasAuthority('ADMIN.GRANTED')")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Object.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Delete an existing order by id")
    @DeleteMapping(Endpoint.Order.ID)
    public ResponseEntity<APIResponse<Boolean>> deleteOrderById(@PathVariable String orderId){
        return ResponseEntity.ok(orderService.deleteOrder(orderId));
    }

    @PreAuthorize("hasAuthority('MANAGE_ORDER.ORDER.UPDATE') OR hasAuthority('ADMIN.GRANTED')")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Object.class), mediaType = "application/json"))
    @SecuredSwaggerOperation(summary = "Update order status")
    @PostMapping(Endpoint.Order.STATUS_ID)
    public ResponseEntity<?> changeOrderStatusById(@PathVariable String orderId,@RequestBody ChangeOrderStatus changeOrderStatus){
        try {
            return ResponseEntity.ok(orderService.changeStatusOrder(orderId, changeOrderStatus));
        }catch (Exception ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
