package com.ct4health.t4health.service.impl;

import com.ct4health.t4health.dto.request.CustomerRequest;
import com.ct4health.t4health.dto.response.CustomerResponse;
import com.ct4health.t4health.entities.Customer;
import com.ct4health.t4health.exception.CustomerNotFoundException;
import com.ct4health.t4health.mapper.CustomerMapper;
import com.ct4health.t4health.repository.CustomerRepository;
import com.ct4health.t4health.service.SmsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

class CustomerServiceImpTest {

    private CustomerRepository customerRepository;
    private SmsService smsService;
    private CustomerServiceImp customerService;

    @BeforeEach
    void setUp() {
        customerRepository = mock(CustomerRepository.class);
        smsService = mock(SmsService.class);
        customerService = new CustomerServiceImp(customerRepository, smsService);
    }

    private CustomerRequest buildRequest() {
        return CustomerRequest.builder()
                .name("Alex")
                .identificationNumber("12345")
                .dateOfBirth(LocalDate.of(1995, 3, 20))
                .gender(com.ct4health.t4health.enums.Gender.MALE)
                .residentialAddress("Nairobi")
                .phoneNumber("+254700123456")
                .email("alex@test.com")
                .build();
    }

    private Customer buildCustomer() {
        return Customer.builder()
                .id("1")
                .name("Alex")
                .identificationNumber("12345")
                .dateOfBirth(LocalDate.of(1995, 3, 20))
                .gender(com.ct4health.t4health.enums.Gender.MALE)
                .residentialAddress("Nairobi")
                .phoneNumber("+254700123456")
                .email("alex@test.com")
                .deleted(false)
                .build();
    }

    @Test
    void registerCustomer_success() {
        CustomerRequest request = buildRequest();
        Customer savedCustomer = buildCustomer();

        // Mock duplicate checks
        when(customerRepository.existsByIdentificationNumber("12345")).thenReturn(false);
        when(customerRepository.existsByEmail("alex@test.com")).thenReturn(false);
        when(customerRepository.existsByPhoneNumber("+254700123456")).thenReturn(false);

        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);

        CustomerResponse response = customerService.registerCustomer(request);

        assertNotNull(response);
        assertEquals("Alex", response.getName());

        verify(smsService, times(1))
                .sendSms(eq("+254700123456"), anyString());
    }

    @Test
    void getCustomerById_success() {
        Customer customer = buildCustomer();
        when(customerRepository.findByIdAndDeletedFalse("1")).thenReturn(Optional.of(customer));

        CustomerResponse response = customerService.getCustomerById("1");

        assertNotNull(response);
        assertEquals("Alex", response.getName());
    }

    @Test
    void getCustomerById_notFound() {
        when(customerRepository.findByIdAndDeletedFalse("1"))
                .thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () ->
                customerService.getCustomerById("1")
        );
    }

    @Test
    void updateCustomer_success() {
        Customer existing = buildCustomer();
        Customer updated = buildCustomer();
        updated.setName("New Name");

        CustomerRequest request = buildRequest();
        request.setName("New Name");

        when(customerRepository.findByIdAndDeletedFalse("1"))
                .thenReturn(Optional.of(existing));

        when(customerRepository.save(existing)).thenReturn(updated);

        CustomerResponse response = customerService.updateCustomer("1", request);

        assertEquals("New Name", response.getName());
    }

    @Test
    void softDeleteCustomer_success() {
        Customer customer = buildCustomer();

        when(customerRepository.findByIdAndDeletedFalse("1"))
                .thenReturn(Optional.of(customer));

        customerService.softDeleteCustomer("1");

        assertTrue(customer.isDeleted());
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void softDeleteCustomer_notFound() {
        when(customerRepository.findByIdAndDeletedFalse("1"))
                .thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () ->
                customerService.softDeleteCustomer("1")
        );
    }

    @Test
    void getAllCustomers_success() {
        Customer c1 = buildCustomer();
        Customer c2 = buildCustomer();
        c2.setId("2");

        when(customerRepository.findAll()).thenReturn(List.of(c1, c2));

        List<CustomerResponse> list = customerService.getAllCustomers();

        assertEquals(2, list.size());
    }
}
