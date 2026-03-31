package com.miku.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TurnstileResponse {
    private boolean success;

    @JsonProperty("error-codes")
    private String[] errorCodes;
}