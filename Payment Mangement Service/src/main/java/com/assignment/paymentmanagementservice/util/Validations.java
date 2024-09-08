package com.assignment.paymentmanagementservice.util;

import com.assignment.paymentmanagementservice.constants.Constants;
import com.assignment.paymentmanagementservice.constants.OrderStatus;
import com.assignment.paymentmanagementservice.constants.PaymentStatus;
import com.assignment.paymentmanagementservice.dto.CustomerDto;
import com.assignment.paymentmanagementservice.dto.OrderDto;
import com.assignment.paymentmanagementservice.entities.Order;
import com.assignment.paymentmanagementservice.entities.Payment;
import com.assignment.paymentmanagementservice.exceptions.CustomGenericException;
import com.assignment.paymentmanagementservice.repositories.CustomerRepo;
import com.assignment.paymentmanagementservice.repositories.OrderRepo;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Component
public class Validations {
    @Autowired
    OrderRepo orderRepo;
    @Autowired
    CustomerRepo customerRepo;

    public void validateOrder(OrderDto order) {
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new CustomGenericException(Constants.FORBIDDEN_CODE, Constants.CANNOT_PASS_STATUS);
        }
        if (order.getPaymentMode() == null) {
            throw new CustomGenericException(Constants.BAD_REQUEST_CODE, Constants.SELECT_PAYMENT_MODE);
        }

    }

    public void validateNewOrder(OrderDto order) {
        if (order.getCustomer() == null) {
            throw new CustomGenericException(Constants.BAD_REQUEST_CODE, Constants.ENTER_CUSTOMER_DETAILS);
        }
        if (order.getAmount() == null) {
            throw new CustomGenericException(Constants.BAD_REQUEST_CODE, Constants.AMOUNT_IS_NULL);
        }

    }

    public void validateNewOrderNewCustomer(OrderDto order) {
        if (order.getCustomer().getName() == null) {
            throw new CustomGenericException(Constants.BAD_REQUEST_CODE, Constants.PHONE_NUMBER_NOT_EXISTS_ERROR);
        }
    }

    public void validateExistingOrder(OrderDto order) {
        if (!orderRepo.existsByOrderId(order.getOrderId())) {
            throw new CustomGenericException(Constants.BAD_REQUEST_CODE, Constants.NO_ORDER_FOUND);

        }
    }

    public void validateAmountForExistingOrder(OrderDto order, OrderDto existingOrder) {
        if (order.getAmount() != null && !existingOrder.getAmount().equals(order.getAmount())) {
            throw new CustomGenericException(Constants.BAD_REQUEST_CODE, Constants.AMOUNT_CANNOT_BE_CHANGED);
        }
    }


    public void validateOrderExists(Order order) {
        if (order == null) {
            throw new CustomGenericException(Constants.BAD_REQUEST_CODE, Constants.NO_ORDER_FOUND);
        }
    }

    public void validateCustomer(long id) {
        if (!customerRepo.findById(id).isPresent()) {
            throw new CustomGenericException(Constants.BAD_REQUEST_CODE, Constants.NO_CUSTOMER_FOUND);
        }

    }


    public void validatePayments(Payment payment) {
        if (payment == null) {
            throw new CustomGenericException(Constants.BAD_REQUEST_CODE, Constants.NO_PAYMENT_WITH_TRANSACTIONID);
        }
        if (payment.getOrder().getStatus() == OrderStatus.ORDER_CONFIRMED) {
            throw new CustomGenericException(Constants.BAD_REQUEST_CODE, Constants.ORDER_ALREADY_CONFIRMED);
        }
    }

    public void validatePaymentStatus(PaymentStatus currentStatus) {
        if (currentStatus.ordinal() >= PaymentStatus.SUCCESS.ordinal()) {
            throw new CustomGenericException(Constants.FORBIDDEN_CODE, Constants.PAYMENT_STATUS_FORBIDDEN);
        }
    }

    public void validatePaymentsFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new CustomGenericException(Constants.BAD_REQUEST_CODE, Constants.UPLOAD_FILE_ERROR);
        }
        if (!Objects.equals(file.getContentType(), "text/csv")) {
            throw new CustomGenericException(Constants.BAD_REQUEST_CODE, Constants.PROVIDE_CSV_FILE_ERROR);
        }
    }

    public void validateBeforeSavingCustomer(CustomerDto customerDto) {
        if (customerDto.getName() == null) {
            throw new CustomGenericException(Constants.BAD_REQUEST_CODE, Constants.NO_NAME_ERROR);
        }
    }
}
