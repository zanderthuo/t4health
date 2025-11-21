package com.ct4health.t4health.service.impl;

import com.ct4health.t4health.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SmsServiceImpl implements SmsService {
    @Override
    public void sendSms(String phoneNumber, String message) {
        log.info("📩 Mock SMS Sent to {}: {}", phoneNumber, message);
    }
}
