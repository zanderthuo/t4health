package com.ct4health.t4health.service;

import com.ct4health.t4health.dto.request.CustomerRequest;
import com.ct4health.t4health.dto.response.CustomerResponse;

import java.util.List;

public interface CustomerService {

    CustomerResponse registerCustomer(CustomerRequest customerRequest);

    CustomerResponse getCustomerById(String id);

    CustomerResponse updateCustomer(String id, CustomerRequest request);

    void softDeleteCustomer(String id);

    List<CustomerResponse> getAllCustomers();

}
