package com.ct4health.t4health.dto.request;

import com.ct4health.t4health.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Identification Number is required")
    private String identificationNumber;

    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotBlank(message = "Residential address is required")
    private String residentialAddress;

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^[0-9+]{7,15}$",
            message = "Invalid phone number format"
    )
    private String phoneNumber;

    @NotBlank(message = "Email address is required")
    @Email(message = "Invalid email format")
    private String email;
}
