package com.ct4health.t4health.repository;

import com.ct4health.t4health.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

    Optional<Customer> findByIdentificationNumberAndDeletedFalse(String identificationNumber);

    boolean existsByIdentificationNumber(String identificationNumber);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<Customer> findByIdAndDeletedFalse(String id);
}