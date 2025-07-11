package com.example.SBA_M.mapper;

import com.example.SBA_M.dto.response.ConsultationResponse;
import com.example.SBA_M.dto.response.SimpleAccountResponse;
import com.example.SBA_M.entity.commands.Account;
import com.example.SBA_M.entity.commands.Consultation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ConsultationMapper {

    @Mapping(source = "account", target = "sender")
    @Mapping(source = "consultant", target = "consultant")
    ConsultationResponse toResponse(Consultation consultation);

    SimpleAccountResponse toSimpleAccountResponse(Account account);
}
