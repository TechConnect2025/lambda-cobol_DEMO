package com.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HelloWorldHandlerTest {

    private HelloWorldHandler handler;
    private TestContext context;

    @BeforeEach
    public void setUp() {
        handler = new HelloWorldHandler();
        context = new TestContext();
    }

    @Test
    public void testHandleRequest() {
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
        request.setPath("/lambda-java-hello-world");
        request.setHttpMethod("GET");

        APIGatewayProxyResponseEvent response = handler.handleRequest(request, context);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Hello World from Java 21!"));
    }

    @Test
    public void testResponseHeaders() {
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
        request.setPath("/test");
        request.setHttpMethod("GET");

        APIGatewayProxyResponseEvent response = handler.handleRequest(request, context);

        assertNotNull(response.getHeaders());
        assertEquals("application/json", response.getHeaders().get("Content-Type"));
    }

    /**
     * Simple test implementation of Lambda Context
     */
    public static class TestContext implements Context {
        @Override
        public String getAwsRequestId() {
            return "test-request-id";
        }

        @Override
        public String getLogGroupName() {
            return "test-group";
        }

        @Override
        public String getLogStreamName() {
            return "test-stream";
        }

        @Override
        public String getFunctionName() {
            return "test-function";
        }

        @Override
        public String getFunctionVersion() {
            return "$LATEST";
        }

        @Override
        public String getInvokedFunctionArn() {
            return "arn:aws:lambda:us-east-1:123456789012:function:test-function";
        }

        @Override
        public com.amazonaws.services.lambda.runtime.ClientContext getClientContext() {
            return null;
        }

        @Override
        public com.amazonaws.services.lambda.runtime.CognitoIdentity getCognitoIdentity() {
            return null;
        }

        @Override
        public int getMemoryLimitInMB() {
            return 256;
        }

        @Override
        public int getRemainingTimeInMillis() {
            return 30000;
        }

        @Override
        public void logStartStop(boolean flag) {
        }

        @Override
        public com.amazonaws.services.lambda.runtime.LambdaLogger getLogger() {
            return new com.amazonaws.services.lambda.runtime.LambdaLogger() {
                @Override
                public void log(String message) {
                    System.out.println(message);
                }

                @Override
                public void log(byte[] message) {
                    System.out.write(message, 0, message.length);
                }
            };
        }
    }
}
