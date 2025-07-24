package com.example.SBA_M.service;

import com.example.SBA_M.dto.request.InitPaymentRequest;
import com.example.SBA_M.dto.response.InitPaymentResponse;

public interface PaymentService {
    InitPaymentResponse init(InitPaymentRequest request);
}
