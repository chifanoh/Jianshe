package com.miku.service;

public interface TurnstileService {
    /**
     * Cloudflare Turnstile人机验证
     * @param tokens
     * @param ip
     * @return
     */
    boolean verify(String tokens, String ip);
}
