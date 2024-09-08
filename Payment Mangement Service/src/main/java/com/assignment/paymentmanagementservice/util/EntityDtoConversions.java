package com.assignment.paymentmanagementservice.util;

import com.assignment.paymentmanagementservice.dto.CustomerDto;
import com.assignment.paymentmanagementservice.dto.OrderDto;
import com.assignment.paymentmanagementservice.dto.PaymentDto;
import com.assignment.paymentmanagementservice.entities.Customer;
import com.assignment.paymentmanagementservice.entities.Order;
import com.assignment.paymentmanagementservice.entities.Payment;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class EntityDtoConversions {
    ModelMapper modelMapper;

    public Payment paymentDtoToEntity(PaymentDto paymentDto) {
        if (paymentDto == null) {
            return null;
        }
        return modelMapper.map(paymentDto, Payment.class);
    }

    public PaymentDto paymentEntityToDto(Payment payment) {
        if (payment == null) return null;
        return modelMapper.map(payment, PaymentDto.class);
    }

    public Order orderDtoToEntity(OrderDto orderDto) {
        if (orderDto == null) {
            return null;
        }
        return modelMapper.map(orderDto, Order.class);
    }

    public OrderDto orderEntityToDto(Order order) {
        if (order == null) return null;
        return modelMapper.map(order, OrderDto.class);
    }

    public Customer customerDtoToEntity(CustomerDto customerDto) {
        if (customerDto == null) return null;
        return modelMapper.map(customerDto, Customer.class);
    }

    public CustomerDto customerEntityToDto(Customer customer) {
        if (customer == null) return null;
        return modelMapper.map(customer, CustomerDto.class);
    }
}
