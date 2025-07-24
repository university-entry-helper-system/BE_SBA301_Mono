package com.example.SBA_M.controller;


import com.example.SBA_M.dto.response.IpnResponse;
import com.example.SBA_M.service.IpnHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/payments")
@Slf4j
@RequiredArgsConstructor
public class PaymentController {

    private final IpnHandler ipnHandler;

    //api để gửi trạng thái giao dịch và trạng thái payment
    //có những cái param sẽ đc ký qua bên vnpay và k thể sửa đổi đc bởi bên thứ 3
    @GetMapping("/vnpay_ipn")
    IpnResponse processIpn(@RequestParam Map<String, String> params) {
        log.info("[VNPay Ipn] Params: {}", params);
        return ipnHandler.process(params);
    }
}
