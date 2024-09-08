package com.assignment.paymentmanagementservice.entities;

import com.assignment.paymentmanagementservice.constants.OrderStatus;
import com.assignment.paymentmanagementservice.constants.PaymentModes;
import lombok.*;
import lombok.experimental.FieldDefaults;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ORDERS")
@Getter@Setter@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    @Column(unique = true)
    String orderId;

    @ManyToOne
    @JoinColumn(name = "customerId")
    Customer customer;

    Double amount;

    @Enumerated(EnumType.STRING)
    PaymentModes paymentMode;

    @Enumerated(EnumType.STRING)
    OrderStatus status = OrderStatus.PENDING;
}
