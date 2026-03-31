package com.miku.service.Impl;

import com.miku.response.TurnstileResponse;
import com.miku.service.TurnstileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class TurnstileServiceImpl implements TurnstileService {
    private static final String VERIFY_URL = "https://challenges.cloudflare.com/turnstile/v0/siteverify";

    @Value("${turnstile.secret.key}")
    private String secretKey;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Cloudflare Turnstile人机验证
     * @param tokens
     * @param remoteIp
     * @return
     */
    public boolean verify(String tokens, String remoteIp) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("secret", secretKey);
        params.add("response", tokens);
        if (remoteIp != null) {
            params.add("remoteip", remoteIp);
        }
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        TurnstileResponse response = restTemplate.postForObject(VERIFY_URL, request, TurnstileResponse.class);
        return response != null && response.isSuccess();
    }
}
