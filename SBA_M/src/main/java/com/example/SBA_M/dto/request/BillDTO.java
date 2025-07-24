package com.example.SBA_M.dto.request;

import com.example.SBA_M.utils.BillStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BillDTO {
    LocalDate billDate;
    Instant billTime;
    Double total;
    BillStatus status;
    AccountCreationRequest user;
    String ipAddress;

}
