package com.assignment.paymentmanagementservice.dto;

import com.assignment.paymentmanagementservice.constants.Constants;
import com.assignment.paymentmanagementservice.constants.PaymentModes;
import com.assignment.paymentmanagementservice.constants.PaymentStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class PaymentDto {
    long id;
    String transactionId;
    @Valid
    @Min(value = 0, message = Constants.AMOUNT_VALIDATION)
    double amount;
    PaymentStatus status = PaymentStatus.PENDING;
    OrderDto order;
    PaymentModes paymentMode;
}
