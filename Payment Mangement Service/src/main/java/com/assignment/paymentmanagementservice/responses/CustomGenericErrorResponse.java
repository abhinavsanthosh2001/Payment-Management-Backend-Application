package com.assignment.paymentmanagementservice.responses;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomGenericErrorResponse {
    String errorCode;
    String message;
}
