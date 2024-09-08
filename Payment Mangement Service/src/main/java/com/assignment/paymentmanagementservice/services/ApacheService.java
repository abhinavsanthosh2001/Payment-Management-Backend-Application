package com.assignment.paymentmanagementservice.services;

import com.assignment.paymentmanagementservice.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;


public interface ApacheService {

    ResponseEntity<ApiResponse> saveBulkPaymentsApache(MultipartFile multipartFile);

    ResponseEntity<Object> updateBulkStatus(MultipartFile multipartFile);


}
