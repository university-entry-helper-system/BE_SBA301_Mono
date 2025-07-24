package com.example.SBA_M.service;

import com.example.SBA_M.dto.request.BillDTO;
import com.example.SBA_M.dto.request.BillRequest;
import com.example.SBA_M.dto.response.BillResponse;

import java.io.IOException;

public interface BillSevice {
    public BillResponse processPayment(final BillRequest request);
    public BillDTO markBooked(Long billId) throws IOException;
    }
