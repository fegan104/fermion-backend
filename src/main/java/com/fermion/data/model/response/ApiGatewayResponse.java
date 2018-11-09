package com.fermion.data.model.response;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by @author frankegan on 11/6/18.
 */
public class ApiGatewayResponse {

    private int statusCode;
    private Map<String, String> headers;
    private String body;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public ApiGatewayResponse(int statusCode, Map<String, String> headers, String body) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.body = body;
    }

    public ApiGatewayResponse(int statusCode, String body) {
        this.statusCode = statusCode;
        this.headers = new HashMap<>();
        headers.put("Content-Type",  "application/json");
        headers.put("Access-Control-Allow-Origin",  "*");
        this.body = body;
    }
}