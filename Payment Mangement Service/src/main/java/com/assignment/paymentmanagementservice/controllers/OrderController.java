package com.assignment.paymentmanagementservice.controllers;

import com.assignment.paymentmanagementservice.dto.OrderDto;
import com.assignment.paymentmanagementservice.responses.ApiResponse;
import com.assignment.paymentmanagementservice.services.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping(value = "/order")
@RequiredArgsConstructor
public class OrderController {
    OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDto> saveOrder(@Valid @RequestBody OrderDto order) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.saveOrder(order));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrderWithId(@PathVariable String orderId) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrderById(orderId));
    }

    @GetMapping("/phoneNumber/{phoneNumber}")
    public ResponseEntity<List<OrderDto>> getOrdersWithPNo(@PathVariable String phoneNumber) {
        return ResponseEntity.ok().body(orderService.getOrdersWithPhoneNumber(phoneNumber));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<List<OrderDto>> getOrdersWithEmail(@PathVariable String email) {
        return ResponseEntity.ok().body(orderService.getOrdersWithEmail(email));
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<ApiResponse> deleteAll() {
        orderService.deleteAll();
        return new ResponseEntity<>(new ApiResponse("Orders deleted", "Completed"), HttpStatus.OK);
    }
}
