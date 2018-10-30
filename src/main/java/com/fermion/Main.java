package com.fermion;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by @author frankegan on 10/30/18.
 */
public class Main implements RequestHandler<Map<String, Object>, APIGatewayResponse> {

    @Override
    public APIGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("Loading Java Lambda handler of ProxyWithStream");

        // annoyance to ensure integration with S3 can support CORS
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Access-Control-Allow-Origin", "*");

        // get from input OR just act on input as if it had been qsp
        try {
            Map<String, Object> queryStringParameters = (Map<String, Object>) input.get("queryStringParameters");

            return new APIGatewayResponse(200, headers, "Hello, World");
        } catch (Exception e) {
            logger.log(e.toString());
            return new APIGatewayResponse(422, headers, "Unable to process input");
        }
    }
}
