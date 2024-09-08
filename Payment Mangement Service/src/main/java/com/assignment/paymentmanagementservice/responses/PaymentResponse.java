package com.assignment.paymentmanagementservice.responses;

import com.assignment.paymentmanagementservice.constants.PaymentModes;
import com.assignment.paymentmanagementservice.constants.PaymentStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentResponse {
    String orderId;
    String transactionId;
    double amount;
    PaymentModes paymentMode;
    PaymentStatus paymentStatus;
}
