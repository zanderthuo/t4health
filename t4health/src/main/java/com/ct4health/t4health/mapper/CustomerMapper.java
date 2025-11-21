package com.ct4health.t4health.mapper;

import com.ct4health.t4health.dto.request.CustomerRequest;
import com.ct4health.t4health.dto.response.CustomerResponse;
import com.ct4health.t4health.entities.Customer;

public class CustomerMapper {

    /**
     * Convert CustomerRequest → Customer (used for CREATE)
     */
    public static Customer toEntity(CustomerRequest request) {
        if (request == null) return null;

        return Customer.builder()
                .name(request.getName())
                .identificationNumber(request.getIdentificationNumber())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .residentialAddress(request.getResidentialAddress())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .build();
    }

    /**
     * Map updates from CustomerRequest → existing Customer (UPDATE)
     */
    public static void updateEntity(Customer customer, CustomerRequest request) {
        customer.setName(request.getName());
        customer.setIdentificationNumber(request.getIdentificationNumber());
        customer.setDateOfBirth(request.getDateOfBirth());
        customer.setGender(request.getGender());
        customer.setResidentialAddress(request.getResidentialAddress());
        customer.setPhoneNumber(request.getPhoneNumber());
        customer.setEmail(request.getEmail());
    }

    /**
     * Convert Customer → CustomerResponse (used for returning API response)
     */
    public static CustomerResponse toResponse(Customer customer) {
        if (customer == null) return null;

        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .identificationNumber(customer.getIdentificationNumber())
                .dateOfBirth(customer.getDateOfBirth())
                .gender(customer.getGender())
                .residentialAddress(customer.getResidentialAddress())
                .phoneNumber(customer.getPhoneNumber())
                .email(customer.getEmail())
                .deleted(customer.isDeleted())
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .build();
    }
}
