package com.example.SBA_M.service;


import com.example.SBA_M.dto.response.IpnResponse;

import java.util.Map;

public interface IpnHandler {
    IpnResponse process(Map<String, String> params);
}