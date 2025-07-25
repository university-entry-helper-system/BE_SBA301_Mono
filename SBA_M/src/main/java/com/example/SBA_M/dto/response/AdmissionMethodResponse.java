package com.example.SBA_M.dto.response;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdmissionMethodResponse {
    private Integer id;
    private String name;
    private String description;
    private com.example.SBA_M.utils.Status status;
}
