package com.assignment.paymentmanagementservice.dto;

import com.assignment.paymentmanagementservice.constants.Constants;
import com.assignment.paymentmanagementservice.constants.OrderStatus;
import com.assignment.paymentmanagementservice.constants.PaymentModes;
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties("id")
public class OrderDto {
    long id;
    String orderId;
    @Valid
    CustomerDto customer;
    @Valid
    @Min(value = 0,message = Constants.AMOUNT_VALIDATION)
    Double amount;
    @Valid
    @NotNull(message = Constants.SELECT_PAYMENT_MODE)
    @JsonEnumDefaultValue
    PaymentModes paymentMode;

    OrderStatus status = OrderStatus.PENDING;
}
