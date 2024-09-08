package com.assignment.paymentmanagementservice.services;

import com.assignment.paymentmanagementservice.util.UploadHelper;
import com.assignment.paymentmanagementservice.constants.Constants;
import com.assignment.paymentmanagementservice.exceptions.CustomGenericException;
import com.assignment.paymentmanagementservice.responses.ApiResponse;
import com.assignment.paymentmanagementservice.util.Validations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
public class ApacheImpl implements ApacheService {
    @Autowired
    Validations validations;
    @Autowired
    UploadHelper uploadHelper;

    @Override
    public ResponseEntity<ApiResponse> saveBulkPaymentsApache(MultipartFile multipartFile) {
        validations.validatePaymentsFile(multipartFile);
        try {
            String originalFilename = multipartFile.getOriginalFilename();
            File fileToImport = new File(Constants.TEMP_STORAGE + originalFilename);
            multipartFile.transferTo(fileToImport);
            uploadHelper.uploadHelper(Constants.TEMP_STORAGE + originalFilename);
            return ResponseEntity.ok().body(new ApiResponse(Constants.UPLOAD_STARTED, Constants.PROCESSING));
        } catch (Exception e) {
            throw new CustomGenericException(Constants.INTERNAL_SERVER_ERROR_CODE, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> updateBulkStatus(MultipartFile multipartFile) {
        try {
            validations.validatePaymentsFile(multipartFile);
            String originalFilename = multipartFile.getOriginalFilename();
            File fileToImport = new File(Constants.TEMP_STORAGE + originalFilename);
            multipartFile.transferTo(fileToImport);
            uploadHelper.updateHelper(fileToImport.getPath());
            return ResponseEntity.ok().body(new ApiResponse(Constants.UPDATE_STARTED, Constants.PROCESSING));
        } catch (Exception e) {
            throw new CustomGenericException(Constants.INTERNAL_SERVER_ERROR_CODE, e.getMessage());
        }
    }


}