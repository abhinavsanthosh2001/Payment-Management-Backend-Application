package com.assignment.paymentmanagementservice.exceptions;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomGenericException extends RuntimeException {
    String errorCode;
    String message;
}
