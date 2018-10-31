package com.fermion.data.model.response;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by @author frankegan on 9/28/18.
 */
public abstract class ApiResponse {
    private int statusCode;
    private Map<String, String> headers;


    public int getStatusCode() {
        return statusCode;
    }

    static Map<String, String> getHeaders() {
        // annoyance to ensure integration with S3 can support CORS
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Access-Control-Allow-Origin", "*");
        return headers;
    }

    public ApiResponse(int statusCode) {
        this.statusCode = statusCode;
        this.headers = getHeaders();
    }
}
