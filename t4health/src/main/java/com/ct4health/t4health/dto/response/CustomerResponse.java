package com.ct4health.t4health.dto.response;

import com.ct4health.t4health.enums.Gender;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponse {
    private String id;
    private String name;
    private String identificationNumber;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String residentialAddress;
    private String phoneNumber;
    private String email;

    private boolean deleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
