package com.assignment.paymentmanagementservice.util;

import com.assignment.paymentmanagementservice.constants.Constants;
import com.assignment.paymentmanagementservice.constants.PaymentModes;
import com.assignment.paymentmanagementservice.constants.PaymentStatus;
import com.assignment.paymentmanagementservice.dto.CustomerDto;
import com.assignment.paymentmanagementservice.dto.OrderDto;
import com.assignment.paymentmanagementservice.exceptions.CustomGenericException;
import com.assignment.paymentmanagementservice.services.OrderService;
import com.assignment.paymentmanagementservice.services.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDateTime;

@Component
@Slf4j
public class UploadHelper {
    @Autowired
    @Lazy
    OrderService orderService;
    @Autowired
    @Lazy
    PaymentService paymentService;

    @Async("Async")
    public void uploadHelper(String url) {
        try (
                Reader in = new FileReader(url);
                CSVParser csvParser = new CSVParser(in, CSVFormat.DEFAULT)

        ) {
            log.info(Constants.UPLOAD_STARTED);
            for (CSVRecord csvRecord : csvParser) {
                if (!csvRecord.get(0).equals("amount")) {
                    try {
                        OrderDto orderDto = new OrderDto();
                        orderDto.setAmount(Double.parseDouble(csvRecord.get(0)));
                        orderDto.setPaymentMode(PaymentModes.valueOf(csvRecord.get(1)));
                        orderDto.setCustomer(new CustomerDto());
                        orderDto.getCustomer().setName(csvRecord.get(2));
                        orderDto.getCustomer().setPhoneNumber(csvRecord.get(3));
                        orderDto.getCustomer().setEmail(csvRecord.get(4));
                        orderService.saveOrder(orderDto);
                    } catch (CustomGenericException e) {
                        log.error("cannot add element with phone number: " + csvRecord.get(3) + " . " + e.getMessage());
                    }
                }
            }
            log.info(Constants.UPLOAD_FINISHED);
        } catch (FileNotFoundException e) {
            throw new CustomGenericException(Constants.INTERNAL_SERVER_ERROR_CODE, "File Not found");
        } catch (IOException e) {
            throw new CustomGenericException(Constants.INTERNAL_SERVER_ERROR_CODE, e.getMessage());
        }
    }

    @Async("Async")
    public void updateHelper(String url) {
        try (
                Reader in = new FileReader(url);
                CSVParser csvParser = new CSVParser(in, CSVFormat.DEFAULT)
        ) {
            log.info(Constants.UPDATE_STARTED);
            for (CSVRecord csvRecord : csvParser) {
                if (!csvRecord.get(0).equals("transactionId")) {
                    try {
                        paymentService.updateStatus(csvRecord.get(0), PaymentStatus.valueOf((csvRecord.get(1))));
                    } catch (CustomGenericException e) {
                        log.error("Cannot insert row with transactionId:" + csvRecord.get(0) + ". " + e.getMessage());

                    }
                }
            }
            log.info(Constants.UPDATE_SUCCESSFUL);
        } catch (FileNotFoundException e) {
            throw new CustomGenericException(Constants.INTERNAL_SERVER_ERROR_CODE, Constants.FILE_NOT_FOUND);
        } catch (IOException e) {
            throw new CustomGenericException(Constants.INTERNAL_SERVER_ERROR_CODE, e.getMessage());
        }
    }
}
