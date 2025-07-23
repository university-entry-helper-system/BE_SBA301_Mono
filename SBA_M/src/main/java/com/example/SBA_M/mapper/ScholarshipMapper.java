package com.example.SBA_M.mapper;

import com.example.SBA_M.dto.request.ScholarshipRequest;
import com.example.SBA_M.dto.response.ScholarshipResponse;
import com.example.SBA_M.entity.commands.Scholarship;
import com.example.SBA_M.entity.commands.University;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ScholarshipMapper {

    public static Scholarship mapToEntity(ScholarshipRequest r) {
        Scholarship s = new Scholarship();
        s.setName(r.getName());
        s.setDescription(r.getDescription());
        s.setValueType(r.getValueType());
        s.setValueAmount(r.getValueAmount());
        s.setEligibilityType(r.getEligibilityType());
        s.setMinScore(r.getMinScore());
        s.setApplyLink(r.getApplyLink());
        s.setApplicationDeadline(r.getApplicationDeadline());
        return s;
    }

    public static ScholarshipResponse mapToResponse(Scholarship s) {
        ScholarshipResponse r = new ScholarshipResponse();
        r.setId(s.getId());
        r.setName(s.getName());
        r.setDescription(s.getDescription());
        r.setValueType(s.getValueType());
        r.setValueAmount(s.getValueAmount());
        r.setEligibilityType(s.getEligibilityType());
        r.setMinScore(s.getMinScore());
        r.setApplyLink(s.getApplyLink());
        r.setApplicationDeadline(s.getApplicationDeadline());

        // status theo deadline
        r.setStatus(s.getApplicationDeadline() != null && s.getApplicationDeadline().isAfter(Instant.now()) ? "ACTIVE" : "EXPIRED");

        // map university info
        List<ScholarshipResponse.UniversityShort> uniList = s.getUniversities().stream().map(u -> {
            ScholarshipResponse.UniversityShort us = new ScholarshipResponse.UniversityShort();
            us.setId(u.getId());
            return us;
        }).collect(Collectors.toList());
        r.setUniversities(uniList);

        return r;
    }

    public static List<ScholarshipResponse> mapToResponseList(List<Scholarship> list) {
        return list.stream().map(ScholarshipMapper::mapToResponse).collect(Collectors.toList());
    }
}
