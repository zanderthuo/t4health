package com.ct4health.t4health.controller;

import com.ct4health.t4health.dto.request.CustomerRequest;
import com.ct4health.t4health.dto.response.CustomerResponse;
import com.ct4health.t4health.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Customer Management", description = "API endpoints for customer registration and management")
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @Operation(summary = "Create a new customer", description = "Registers a new customer and sends a welcome SMS.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Customer created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request or duplicate data",
                    content = @Content),
    })
    @PostMapping("/create")
    public ResponseEntity<CustomerResponse> registerCustomer(
            @Valid @RequestBody CustomerRequest request) {

        CustomerResponse response = customerService.registerCustomer(request);
        return ResponseEntity.status(201).body(response);
    }

    @Operation(summary = "Get customer by ID", description = "Fetches a customer's details using their unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerResponse.class))),
            @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content)
    })
    @GetMapping("/get/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable String id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @Operation(summary = "List all customers", description = "Retrieves all active (non-deleted) customers.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customers retrieved successfully",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/list")
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @Operation(summary = "Update customer details", description = "Updates a customer's information using their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerResponse.class))),
            @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content)
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(
            @PathVariable String id,
            @Valid @RequestBody CustomerRequest request) {

        return ResponseEntity.ok(customerService.updateCustomer(id, request));
    }

    @Operation(summary = "Soft delete a customer", description = "Marks a customer as deleted without removing them from the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Customer deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content)
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable String id) {
        customerService.softDeleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
