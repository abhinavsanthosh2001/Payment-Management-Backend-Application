package com.assignment.paymentmanagementservice.dto;

import com.assignment.paymentmanagementservice.constants.Constants;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties("customerId")
public class CustomerDto {

    long customerId;

    @Pattern(regexp = "^[A-Za-z\\s]*$", message = Constants.NAME_VALIDATION)
    String name;


    @Pattern(regexp = "^\\d{10}$", message = Constants.PHONE_NUMBER_VALIDATION)
    @NotBlank(message = Constants.PHONE_NUMBER_BLANK)
    String phoneNumber;

    @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message = Constants.EMAIL_VALIDATION)
    String email;


}
