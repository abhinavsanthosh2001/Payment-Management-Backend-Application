package com.assignment.paymentmanagementservice.entities;

import com.assignment.paymentmanagementservice.util.AesEncryptor;
import lombok.*;
import lombok.experimental.FieldDefaults;
import javax.persistence.*;

@Entity
@Table(name = "CUSTOMERS")
@Getter@Setter@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long customerId;
    String name;
    @Column(unique = true)
    @Convert(converter = AesEncryptor.class)
    String phoneNumber;
    @Convert(converter = AesEncryptor.class)
    String email;
}