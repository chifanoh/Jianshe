package com.miku.utils;

public class LoginUtil {
    public static boolean isEmail(String str) {
        return str != null && str.matches("^[1-9]\\d{4,10}@qq\\.com$");
    }
}
