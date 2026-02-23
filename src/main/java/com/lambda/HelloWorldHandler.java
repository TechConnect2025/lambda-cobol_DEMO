package com.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Lambda handler for AWS Lambda that processes API Gateway events.
 * This is the Java 21 equivalent of the original COBOL hello-world application.
 */
public class HelloWorldHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("Handler received request: " + input.getPath());

        // The equivalent message from the original COBOL program
        String message = "Hello World from Java 21!";
        logger.log(message);

        // Create and return API Gateway response
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setStatusCode(200);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        response.setHeaders(headers);

        // Return JSON response
        String body = "{\n  \"message\": \"" + message + "\",\n  \"runtime\": \"Java 21\"\n}";
        response.setBody(body);

        return response;
    }
}
