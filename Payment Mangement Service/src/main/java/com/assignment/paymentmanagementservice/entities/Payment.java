package com.assignment.paymentmanagementservice.entities;

import com.assignment.paymentmanagementservice.constants.Constants;
import com.assignment.paymentmanagementservice.constants.PaymentModes;
import com.assignment.paymentmanagementservice.constants.PaymentStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.io.Serializable;

@Entity
@Table(name = "PAYMENTS")
@Setter@Getter@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Payment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    @Column(unique = true)
    String transactionId;

    @Min(value = 0, message = Constants.AMOUNT_VALIDATION)
    double amount;

    @Enumerated(EnumType.STRING)
    PaymentStatus status = PaymentStatus.PENDING;

    @ManyToOne
    @JoinColumn(referencedColumnName = "orderId", name = "orderId")
    Order order;

    @Enumerated(EnumType.STRING)
    PaymentModes paymentMode;
}
