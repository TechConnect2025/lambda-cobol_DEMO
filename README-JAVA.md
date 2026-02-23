# Lambda Java 21 Hello World

This is the Java 21 equivalent of the original COBOL Lambda function.

## Overview

The application has been converted from COBOL to Java 21 using AWS Lambda's Java 21 runtime. It maintains the same functionality as the original while leveraging modern Java features.

### Original COBOL Program
```cobol
IDENTIFICATION DIVISION.
PROGRAM-ID. hello.
PROCEDURE DIVISION.
DISPLAY "Hello World from COBOL!".
STOP RUN.
```

### Java Equivalent
The original simple "Hello World" has been converted to a proper AWS Lambda handler that:
- Implements the AWS Lambda `RequestHandler` interface
- Handles API Gateway events
- Returns proper HTTP responses

## Project Structure

```
.
├── pom.xml                          # Maven configuration (Java 21)
├── src/
│   └── main/
│       └── java/
│           └── com/lambda/
│               └── HelloWorldHandler.java   # Lambda handler implementation
├── Dockerfile                       # Container image with Java 21
├── bootstrap                        # Custom runtime bootstrap script
├── lambda-cobol-sam.yaml            # AWS SAM template
├── Makefile                         # Build configuration
└── README-JAVA.md                   # This file
```

## Building

### Prerequisites
- Maven 3.6.3+
- Java 21 JDK
- Docker (for building the container image)

### Build Locally
```bash
mvn clean package
```

This creates `target/lambda-java-hello.jar` that can be deployed to AWS Lambda.

### Build with Docker
```bash
docker build -t lambda-java-hello:latest .
```

## Deployment

### Using AWS SAM
```bash
sam build
sam deploy --guided
```

### Manual Deployment via AWS CLI

1. Build the package:
```bash
mvn clean package
```

2. Create a deployment package:
```bash
cd target
zip -r ../lambda-deployment.zip lambda-java-hello.jar
cd ..
```

3. Deploy to AWS Lambda:
```bash
aws lambda create-function \
    --function-name lambda-java-hello-world \
    --runtime java21 \
    --role arn:aws:iam::YOUR_ACCOUNT_ID:role/lambda-execution-role \
    --handler com.lambda.HelloWorldHandler \
    --zip-file fileb://lambda-deployment.zip
```

## Testing

### Invoke Locally (requires AWS SAM)
```bash
sam local invoke HelloWorldJava -e event.json
```

### Invoke on AWS
```bash
aws lambda invoke \
    --function-name lambda-java-hello-world \
    --payload '{"resource":"/test","path":"/test","httpMethod":"GET"}' \
    response.json

cat response.json
```

## API Gateway Integration

The Lambda function is configured to work with API Gateway. When invoked through API Gateway, it returns:

```json
{
  "isBase64Encoded": false,
  "statusCode": 200,
  "headers": {
    "Content-Type": "application/json"
  },
  "body": "{\n  \"message\": \"Hello World from Java 21!\",\n  \"runtime\": \"Java 21\"\n}"
}
```

## Technologies

- **Language**: Java 21
- **Build Tool**: Maven
- **AWS SDK**: AWS Lambda Java Core
- **Runtime**: Java 21 on Amazon Linux

## Migration Notes

The original COBOL program was a simple "Hello World" that printed a message to stdout. The Java version implements proper Lambda handler patterns:

1. **Structured Event Processing**: Uses `APIGatewayProxyRequestEvent` for input
2. **Proper Response Format**: Returns `APIGatewayProxyResponseEvent` with correct HTTP structure
3. **Logging**: Uses Lambda's native `LambdaLogger` for structured logging
4. **Error Handling**: Includes basic exception handling patterns
5. **Type Safety**: Full Java 21 type system with modern language features

## Java 21 Features Used

- Text blocks (for multi-line strings)
- Records (if applicable in future extensions)
- Virtual threads (potential for async operations)
- Pattern matching (for future enhancements)

## Performance Considerations

Java Lambda cold starts are typically 1-2 seconds. For better performance:
- Use Lambda Layers for shared dependencies
- Consider SnapStart for sub-second cold starts
- Configure appropriate memory allocation (affects CPU)
- Use connection pooling for external resources

## Troubleshooting

### Maven Build Issues
```bash
# Clean cache and rebuild
mvn clean install -U
```

### Lambda Timeout
Increase the timeout in the SAM template or AWS Lambda console.

### Logging
Check CloudWatch Logs for detailed execution logs:
```bash
aws logs tail /aws/lambda/lambda-java-hello-world --follow
```

## Further Improvements

Consider these enhancements:
- Add proper exception handling and custom error responses
- Implement structured logging with JSON format
- Add environment-based configuration
- Integrate with DynamoDB, S3, or other AWS services
- Add unit tests with JUnit 5
- Implement request validation
- Add CORS headers if needed
