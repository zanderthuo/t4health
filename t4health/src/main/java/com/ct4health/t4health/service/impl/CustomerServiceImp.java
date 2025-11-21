package com.ct4health.t4health.service.impl;

import com.ct4health.t4health.dto.request.CustomerRequest;
import com.ct4health.t4health.dto.response.CustomerResponse;
import com.ct4health.t4health.entities.Customer;
import com.ct4health.t4health.exception.CustomerNotFoundException;
import com.ct4health.t4health.mapper.CustomerMapper;
import com.ct4health.t4health.repository.CustomerRepository;
import com.ct4health.t4health.service.CustomerService;
import com.ct4health.t4health.service.SmsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImp implements CustomerService {

    private final CustomerRepository customerRepository;
    private final SmsService smsService;

    @Override
    @Transactional
    public CustomerResponse registerCustomer(CustomerRequest request) {

        // Check duplicates
        if (customerRepository.existsByIdentificationNumber(request.getIdentificationNumber())) {
            throw new IllegalArgumentException("Identification Number already registered");
        }

        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        if (customerRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new IllegalArgumentException("Phone number already registered");
        }

        // Map request to entity
        Customer customer = CustomerMapper.toEntity(request);

        // Save to DB
        Customer saved = customerRepository.save(customer);

        // Send welcome SMS
        smsService.sendSms(
                saved.getPhoneNumber(),
                "Welcome to Presto Technologies, " + saved.getName() + "! Your account has been created successfully."
        );

        return CustomerMapper.toResponse(saved);
    }

    @Override
    @Transactional()
    public CustomerResponse getCustomerById(String id) {
        Customer customer = customerRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        return CustomerMapper.toResponse(customer);
    }

    @Override
    @Transactional
    public CustomerResponse updateCustomer(String id, CustomerRequest request) {
        Customer customer = customerRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        // Update fields
        CustomerMapper.updateEntity(customer, request);

        Customer updated = customerRepository.save(customer);

        return CustomerMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void softDeleteCustomer(String id) {
        Customer customer = customerRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        customer.setDeleted(true);
        customerRepository.save(customer);
    }

    @Override
    @Transactional()
    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .filter(c -> !c.isDeleted())
                .map(CustomerMapper::toResponse)
                .toList();
    }

}
