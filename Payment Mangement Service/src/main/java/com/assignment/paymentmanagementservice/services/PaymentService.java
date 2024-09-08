package com.assignment.paymentmanagementservice.services;

import com.assignment.paymentmanagementservice.constants.Constants;
import com.assignment.paymentmanagementservice.util.IdGenerator;
import com.assignment.paymentmanagementservice.constants.OrderStatus;
import com.assignment.paymentmanagementservice.constants.PaymentStatus;
import com.assignment.paymentmanagementservice.dto.PaymentDto;
import com.assignment.paymentmanagementservice.entities.Payment;
import com.assignment.paymentmanagementservice.exceptions.CustomGenericException;
import com.assignment.paymentmanagementservice.repositories.OrderRepo;
import com.assignment.paymentmanagementservice.repositories.PaymentRepo;
import com.assignment.paymentmanagementservice.responses.ApiResponse;
import com.assignment.paymentmanagementservice.responses.PaymentResponse;
import com.assignment.paymentmanagementservice.util.EntityDtoConversions;
import com.assignment.paymentmanagementservice.util.Validations;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentService {
    @Autowired
    PaymentRepo paymentRepo;
    @Lazy
    @Autowired
    @Qualifier("Export")
    Job exportJob;
    @Autowired
    IdGenerator idGenerator;
    @Autowired
    OrderRepo orderRepo;
    @Autowired
    Validations validations;
    @Autowired
    EntityDtoConversions entityDtoConversions;
    @Autowired
    JobLauncher jobLauncher;
    @Autowired
    @Qualifier("getTransaction")
    Job getTransactionsJob;
    @Autowired
    FileService fileService;


    public void savePayment(PaymentDto payment) {
        payment.setTransactionId(idGenerator.transactionId());
        paymentRepo.save(entityDtoConversions.paymentDtoToEntity(payment));
    }

    public List<PaymentResponse> getPaymentsByOrderId(String orderId) {
        List<PaymentDto> paymentDtos = paymentRepo.findByOrderOrderId(orderId).stream().map(entityDtoConversions::paymentEntityToDto).collect(Collectors.toList());
        List<PaymentResponse> paymentResponses = new ArrayList<>();
        for (PaymentDto payment : paymentDtos) {
            paymentResponses.add(new PaymentResponse(payment.getOrder().getOrderId(), payment.getTransactionId(), payment.getAmount(), payment.getPaymentMode(), payment.getStatus()));
        }
        if (paymentResponses.isEmpty()) {
            throw new CustomGenericException(Constants.BAD_REQUEST_CODE, Constants.NO_TRANSACTIONS_FOUND);
        }
        return paymentResponses;
    }

    public ApiResponse updateStatus(String transactionId, PaymentStatus newStatus) {
        Payment payment = getPaymentByTransactionId(transactionId);
        validations.validatePayments(payment);
        PaymentStatus currentStatus = payment.getStatus();
        validations.validatePaymentStatus(currentStatus);
        paymentRepo.updateStatus(payment.getTransactionId(), newStatus.name());
        if (newStatus.ordinal() == 1) {
            orderRepo.updateStatus(OrderStatus.ORDER_CONFIRMED.name(), payment.getOrder().getOrderId());
        }
        return new ApiResponse(Constants.STATUS_UPDATED, Constants.TRUE);

    }

    public Payment getPaymentByTransactionId(String transactionId) {
        return paymentRepo.findByTransactionId(transactionId);
    }

    public void deleteAll() {
        paymentRepo.deleteAll();
    }

    public ResponseEntity<Resource> getBulkTransactions() {
        JobParameters jobParameters = new JobParametersBuilder().addLong(Constants.STARTED_AT, System.currentTimeMillis()).toJobParameters();
        try {
            jobLauncher.run(getTransactionsJob, jobParameters);
            Resource file = fileService.load(Constants.GET_TRANSACTIONS_FILE);
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + file.getFilename() + "\"").body(file);
        } catch (JobExecutionAlreadyRunningException | JobParametersInvalidException |
                 JobInstanceAlreadyCompleteException | JobRestartException e) {
            throw new CustomGenericException(Constants.INTERNAL_SERVER_ERROR_CODE, Constants.GENERAL + e.getMessage());
        }
    }

    public ResponseEntity<Resource> getBulkStatus() {
        JobParameters jobParameters = new JobParametersBuilder().addLong(Constants.STARTED_AT, System.currentTimeMillis()).toJobParameters();
        try {
            jobLauncher.run(exportJob, jobParameters);
            Resource file = fileService.load(Constants.PAYMENTS_FILE);
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + file.getFilename() + "\"").body(file);
        } catch (JobExecutionAlreadyRunningException | JobParametersInvalidException |
                 JobInstanceAlreadyCompleteException | JobRestartException e) {
            throw new CustomGenericException(Constants.INTERNAL_SERVER_ERROR_CODE, e.getMessage());
        }
    }

}

