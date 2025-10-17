package org.example.bookvexebej2e.configs;

import lombok.Data;

@Data
public class CustomErrorResponse {
    private String timestamp;

    private int status;

    private String error;

    private String message;

    private String trace;

    private String path;
}