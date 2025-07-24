package com.example.SBA_M.service.impl;

import com.example.SBA_M.dto.request.AccountCreationRequest;
import com.example.SBA_M.dto.request.BillDTO;
import com.example.SBA_M.dto.request.BillRequest;
import com.example.SBA_M.dto.request.InitPaymentRequest;
import com.example.SBA_M.dto.response.BillResponse;
import com.example.SBA_M.entity.Bill;
import com.example.SBA_M.entity.Payment;
import com.example.SBA_M.entity.commands.Account;
import com.example.SBA_M.exception.AppException;
import com.example.SBA_M.exception.ErrorCode;
import com.example.SBA_M.repository.commands.AccountRepository;
import com.example.SBA_M.repository.commands.BillRepository;
import com.example.SBA_M.repository.commands.PaymentRepository;
import com.example.SBA_M.service.BillService;
import com.example.SBA_M.service.PaymentService;
import com.example.SBA_M.utils.BillStatus;
import com.example.SBA_M.utils.PaymentStatus;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BillServiceImpl implements BillService {
    PaymentService paymentService;
    BillRepository billRepository;
    AccountRepository accountRepository;
    PaymentRepository paymentRepository;
@Transactional
public BillResponse processPayment(final BillRequest request) {
    String requestId = UUID.randomUUID().toString();

    var context = SecurityContextHolder.getContext();
    String name = context.getAuthentication().getName();
    Account user = accountRepository.findByEmail(name).orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));

    Double totalAmount = 10000000.00;
    Bill bill = Bill.builder()
            .billDate(LocalDate.now())
            .billTime(Instant.now())
            .billStatus(BillStatus.PENDING)
            .total(totalAmount)
            .user(user)
            .build();
    billRepository.save(bill);

    bill.setBillStatus(BillStatus.PENDING);
    billRepository.save(bill);

    BigDecimal bigDecimalValue = new BigDecimal(String.valueOf(totalAmount));
    var initPayment = Payment.builder()
            .bill(bill)
            .amount(bigDecimalValue)
            .currency("VND")
            .status(PaymentStatus.PENDING)
            .paymentDate(LocalDateTime.now())
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    paymentRepository.save(initPayment);

    var initPaymentRequest = InitPaymentRequest.builder()
            .userId(Long.valueOf(bill.getUser().getId().toString()))
            .amount(bill.getTotal().longValue())
            .txnRef(String.valueOf(bill.getId()))
            .requestId(requestId)
            .ipAddress(UUID.randomUUID().toString())
            .build();
    var initPaymentResponse = paymentService.init(initPaymentRequest);

    var billDto = mapToDto(bill);
    log.info("[request_id={}] User user_id={} created booking_id={} successfully", requestId, bill.getUser().getId(), bill.getId());
    return BillResponse.builder()
            .bill(billDto)
            .payment(initPaymentResponse)
            .build();
}

    @Transactional
    public BillDTO markBooked(Long billId) throws IOException {
        final var billOpt = billRepository.findById(billId);
        if (billOpt.isEmpty()) {
            throw new AppException(ErrorCode.BILL_NOT_FOUND);
        }

        final var bill = billOpt.get();
        Payment payment = bill.getPayment();
        bill.setBillStatus(BillStatus.PAID);
        payment.setStatus(PaymentStatus.SUCCESS);
        billRepository.save(bill);
        paymentRepository.save(payment);
        return mapToDto(bill);
    }

    public BillDTO getBookingStatus(Long billId) {
        final var billOpt = billRepository.findById(billId);
        if (billOpt.isEmpty()) {
            throw new AppException(ErrorCode.BILL_NOT_FOUND);
        }

        return mapToDto(billOpt.get());
    }
    private BillDTO mapToDto(Bill bill) {
        AccountCreationRequest userDto = AccountCreationRequest.builder()
                .email(bill.getUser().getEmail())
                .username(bill.getUser().getUsername())
                .fullName(bill.getUser().getFullName())
                .phone(bill.getUser().getPhone())
                .build();

        return BillDTO.builder()
                .billDate(bill.getBillDate())
                .billTime(bill.getBillTime())
                .status(bill.getBillStatus())
                .total(bill.getTotal())
                .user(userDto)
                .ipAddress(UUID.randomUUID().toString())
                .build();
    }
}

