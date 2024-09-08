package com.assignment.paymentmanagementservice.services;

import com.assignment.paymentmanagementservice.constants.Constants;
import com.assignment.paymentmanagementservice.dto.CustomerDto;
import com.assignment.paymentmanagementservice.entities.Customer;
import com.assignment.paymentmanagementservice.exceptions.CustomGenericException;
import com.assignment.paymentmanagementservice.repositories.CustomerRepo;
import com.assignment.paymentmanagementservice.util.EntityDtoConversions;
import com.assignment.paymentmanagementservice.util.Validations;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CustomerService {
    CustomerRepo customerRepo;
    Validations validations;
    EntityDtoConversions entityDtoConversions;

    public CustomerDto saveCustomer(CustomerDto customerDto) {
        validations.validateBeforeSavingCustomer(customerDto);
        try {
            return entityDtoConversions.customerEntityToDto(customerRepo.save(entityDtoConversions.customerDtoToEntity(customerDto)));
        } catch (IllegalArgumentException e) {
            throw new CustomGenericException(Constants.INTERNAL_SERVER_ERROR_CODE, e.getMessage());
        }
    }

    public CustomerDto getCustomerById(long customerId) {
        Optional<Customer> optionalCustomer = customerRepo.findById(customerId);
        if (!optionalCustomer.isPresent()) {
            throw new CustomGenericException(Constants.BAD_REQUEST_CODE, Constants.NO_CUSTOMER_FOUND);
        }
        return entityDtoConversions.customerEntityToDto(optionalCustomer.get());

    }

    @Caching(evict = {
            @CacheEvict(value = "cache", key = "#customerDto.phoneNumber"),
            @CacheEvict(value = "cache", key = "#customerDto.email")
    })
    public CustomerDto update(long id, CustomerDto customerDto) {
        validations.validateCustomer(id);
        customerDto.setCustomerId(id);
        customerRepo.save(entityDtoConversions.customerDtoToEntity(customerDto));
        return customerDto;
    }


    public CustomerDto getCustomerWithPhoneNumber(String phoneNumber) {
        return entityDtoConversions.customerEntityToDto(customerRepo.findByPhoneNumber(phoneNumber));
    }
}
