package com.sai.BGClear.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClearBgResponse {

    private boolean success;
    private HttpStatus statusCode;
    private Object data;
}
