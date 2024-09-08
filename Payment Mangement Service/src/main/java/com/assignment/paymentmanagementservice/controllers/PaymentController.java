package com.assignment.paymentmanagementservice.controllers;

import com.assignment.paymentmanagementservice.constants.PaymentStatus;
import com.assignment.paymentmanagementservice.responses.ApiResponse;
import com.assignment.paymentmanagementservice.responses.PaymentResponse;
import com.assignment.paymentmanagementservice.services.ApacheService;
import com.assignment.paymentmanagementservice.services.PaymentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping(value = "/payment")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PaymentController {
    PaymentService paymentService;
    ApacheService apache;

    @GetMapping("/{orderId}")
    public ResponseEntity<List<PaymentResponse>> getPayments(@PathVariable String orderId) {
        return ResponseEntity.ok().body(paymentService.getPaymentsByOrderId(orderId));
    }

    @PutMapping("/StatusUpdate/{transactionId}/{status}")
    public ResponseEntity<Object> updateStatus(@PathVariable String transactionId,
                                               @PathVariable PaymentStatus status) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(paymentService.updateStatus(transactionId, status));
    }

    @GetMapping("/getStatus")
    public ResponseEntity<Resource> getPaymentStatus() {
        return paymentService.getBulkStatus();
    }

    @GetMapping("/getTransactions")
    public ResponseEntity<Resource> getTransactions() {
        return paymentService.getBulkTransactions();
    }

    @PostMapping("/dumpApache")
    public ResponseEntity<ApiResponse> savePaymentsWithApache(@RequestParam("file") MultipartFile multipartFile) {
        return apache.saveBulkPaymentsApache(multipartFile);
    }

    @PutMapping("/dumpStatusApache")
    public ResponseEntity<Object> updateBulkStatusWithApache(@RequestParam("file") MultipartFile multipartFile) {
        return apache.updateBulkStatus(multipartFile);
    }
}
