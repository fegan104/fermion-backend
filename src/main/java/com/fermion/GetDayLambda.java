package com.fermion;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fermion.data.model.response.ApiGatewayResponse;

import java.util.Map;

/**
 * Created by @author frankegan on 11/9/18.
 */
public class GetDayLambda implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        return null;
    }
}
