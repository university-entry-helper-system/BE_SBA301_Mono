package com.example.SBA_M.service.impl;




import com.example.SBA_M.constant.VNPayParams;
import com.example.SBA_M.constant.VnpIpnResponseConst;
import com.example.SBA_M.dto.response.IpnResponse;
import com.example.SBA_M.exception.AppException;
import com.example.SBA_M.service.BillSevice;
import com.example.SBA_M.service.IpnHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class VNPayIpnHandler implements IpnHandler {

    private final VNPayService vnPayService;
    private final BillSevice billSevice;


    public IpnResponse process(Map<String, String> params) {
        if (!vnPayService.verifyIpn(params)) {
            return VnpIpnResponseConst.SIGNATURE_FAILED;
        }

        IpnResponse response;
        var txnRef = params.get(VNPayParams.TXN_REF);
        try {
            var bookingId = Long.parseLong(txnRef);

            billSevice.markBooked(bookingId);//danh dau booking thanh toan thanh cong
            response = VnpIpnResponseConst.SUCCESS;
        }
        catch (AppException e) {//trả về response code đúng theo quy định của VNPay
            switch (e.getErrorCode()) {
                case BILL_NOT_FOUND -> response = VnpIpnResponseConst.ORDER_NOT_FOUND;
                default -> response = VnpIpnResponseConst.UNKNOWN_ERROR;
            }
        }
        catch (Exception e) {
            response = VnpIpnResponseConst.UNKNOWN_ERROR;
        }

        log.info("[VNPay Ipn] txnRef: {}, response: {}", txnRef, response);
        return response;
    }
}