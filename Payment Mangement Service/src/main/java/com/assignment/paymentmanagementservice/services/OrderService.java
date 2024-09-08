package com.assignment.paymentmanagementservice.services;

import com.assignment.paymentmanagementservice.util.AesEncryptor;
import com.assignment.paymentmanagementservice.constants.Constants;
import com.assignment.paymentmanagementservice.util.IdGenerator;
import com.assignment.paymentmanagementservice.dto.OrderDto;
import com.assignment.paymentmanagementservice.dto.PaymentDto;
import com.assignment.paymentmanagementservice.entities.Order;
import com.assignment.paymentmanagementservice.exceptions.CustomGenericException;
import com.assignment.paymentmanagementservice.repositories.OrderRepo;
import com.assignment.paymentmanagementservice.util.EntityDtoConversions;
import com.assignment.paymentmanagementservice.util.Validations;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class OrderService {
    OrderRepo orderRepo;
    CustomerService customerService;
    PaymentService paymentService;
    IdGenerator idGenerator;
    AesEncryptor aesEncryptor;
    Validations validations;
    EntityDtoConversions entityDtoConversions;

    public OrderDto saveOrder(OrderDto order) {
        validations.validateOrder(order);
        if (order.getOrderId() == null) {
            return processNewOrder(order);
        } else {
            return processExistingOrder(order);
        }
    }



    public OrderDto processNewOrder(OrderDto order) {
        validations.validateNewOrder(order);
        order.setOrderId(idGenerator.orderId());
        if (customerService.getCustomerWithPhoneNumber(order.getCustomer().getPhoneNumber()) == null) {
            validations.validateNewOrderNewCustomer(order);
            order.setCustomer(customerService.saveCustomer(order.getCustomer()));
        } else {
            order.setCustomer((customerService.getCustomerWithPhoneNumber(order.getCustomer().getPhoneNumber())));
        }
        try {
            OrderDto orderResult = entityDtoConversions.orderEntityToDto(orderRepo.save(entityDtoConversions.orderDtoToEntity(order)));
            paymentService.savePayment(createPayment(orderResult));
            return orderResult;

        } catch (Exception e) {
            throw new CustomGenericException(Constants.BAD_REQUEST_CODE, Constants.GENERAL + e.getMessage());
        }
    }

    public OrderDto processExistingOrder(OrderDto order) {
        validations.validateExistingOrder(order);
        OrderDto existingOrder = getOrderById(order.getOrderId());
        validations.validateAmountForExistingOrder(order, existingOrder);
        existingOrder.setPaymentMode(order.getPaymentMode());
        PaymentDto payment = createPayment(existingOrder);
        paymentService.savePayment(payment);
        return getOrderById(order.getOrderId());

    }

    public OrderDto getOrderById(String orderId) {
        Order order = orderRepo.findByOrderId(orderId);
        validations.validateOrderExists(order);
        return entityDtoConversions.orderEntityToDto(order);
    }

    @Cacheable(value = "cache", key = "#phoneNumber")
    public List<OrderDto> getOrdersWithPhoneNumber(String phoneNumber) {
        List<Order> orders = orderRepo.getOrdersWithPhoneNumber(aesEncryptor.convertToDatabaseColumn(phoneNumber));
        if (orders.isEmpty()) {
            throw new CustomGenericException(Constants.BAD_REQUEST_CODE, Constants.NO_ORDERS_WITH_PHONE_NUMBER);
        }
        return orders.stream().map(entityDtoConversions::orderEntityToDto).collect(Collectors.toList());
    }

    @Cacheable(value = "cache", key = "#email")
    public List<OrderDto> getOrdersWithEmail(String email) {
        List<Order> orders = orderRepo.getOrdersWithEmail(aesEncryptor.convertToDatabaseColumn(email));
        if (orders.isEmpty()) {
            throw new CustomGenericException(Constants.BAD_REQUEST_CODE, Constants.NO_ORDERS_WITH_EMAIL);
        }
        return orders.stream().map(entityDtoConversions::orderEntityToDto).collect(Collectors.toList());
    }

    public void deleteAll() {
        paymentService.deleteAll();
        orderRepo.deleteAll();
    }

    public PaymentDto createPayment(OrderDto order) {
        PaymentDto payment = new PaymentDto();
        payment.setPaymentMode(order.getPaymentMode());
        payment.setAmount(order.getAmount());
        payment.setOrder(order);
        return payment;
    }

}


