package com.assignment.paymentmanagementservice.controllers;

import com.assignment.paymentmanagementservice.dto.CustomerDto;
import com.assignment.paymentmanagementservice.services.CustomerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CustomerController {
    CustomerService customerService;

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerDto> getCustomer(@PathVariable long customerId) {
        return ResponseEntity.status(HttpStatus.OK).body(customerService.getCustomerById(customerId));
    }

    @PostMapping
    public ResponseEntity<CustomerDto> saveCustomer(@RequestBody CustomerDto customerDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.saveCustomer(customerDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable long id, @RequestBody CustomerDto customerDto) {
        return ResponseEntity.ok().body(customerService.update(id, customerDto));
    }
}