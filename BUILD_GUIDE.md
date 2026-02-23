# Lambda Java 21 - Build and Deployment Guide

## Quick Start

### Prerequisites
- Java 21 JDK installed
- Maven 3.6.3 or higher
- AWS CLI configured (for deployment)
- Docker (for containerized builds)

### Build Locally

```bash
# Clean and build the project
mvn clean package

# Output will be in target/lambda-java-hello.jar
```

### Run Unit Tests

```bash
mvn test
```

### Build Docker Image

```bash
docker build -t lambda-java-hello:latest .
```

## Deployment Options

### Option 1: AWS Serverless Application Model (SAM)

```bash
# Build
sam build --use-container

# Deploy (interactive)
sam deploy --guided

# Or deploy to existing stack
sam deploy
```

### Option 2: Direct AWS CLI Deployment

```bash
# Build the package
mvn clean package

# Create deployment ZIP
mkdir -p build
cp target/lambda-java-hello.jar build/
cd build
zip -r ../lambda-deployment.zip *
cd ..

# Create execution role (one time)
aws iam create-role --role-name lambda-java-execution-role \
  --assume-role-policy-document file://trust-policy.json

# Attach basic Lambda execution policy
aws iam attach-role-policy --role-name lambda-java-execution-role \
  --policy-arn arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole

# Create the Lambda function
aws lambda create-function \
  --function-name lambda-java-hello-world \
  --runtime java21 \
  --role arn:aws:iam::$(aws sts get-caller-identity --query Account --output text):role/lambda-java-execution-role \
  --handler com.lambda.HelloWorldHandler \
  --zip-file fileb://lambda-deployment.zip \
  --timeout 30 \
  --memory-size 256

# Or update existing function
aws lambda update-function-code \
  --function-name lambda-java-hello-world \
  --zip-file fileb://lambda-deployment.zip
```

### Option 3: Container Image (ECR)

```bash
# Build and tag the image
docker build -t lambda-java-hello:latest .
docker tag lambda-java-hello:latest \
  $(aws sts get-caller-identity --query Account --output text).dkr.ecr.us-east-1.amazonaws.com/lambda-java-hello:latest

# Login to ECR
aws ecr get-login-password --region us-east-1 | docker login \
  --username AWS --password-stdin \
  $(aws sts get-caller-identity --query Account --output text).dkr.ecr.us-east-1.amazonaws.com

# Push image
docker push $(aws sts get-caller-identity --query Account --output text).dkr.ecr.us-east-1.amazonaws.com/lambda-java-hello:latest

# Create Lambda function from image
aws lambda create-function \
  --function-name lambda-java-hello-world \
  --role arn:aws:iam::$(aws sts get-caller-identity --query Account --output text):role/lambda-java-execution-role \
  --code ImageUri=$(aws sts get-caller-identity --query Account --output text).dkr.ecr.us-east-1.amazonaws.com/lambda-java-hello:latest \
  --package-type Image \
  --timeout 30 \
  --memory-size 256
```

## Testing

### Local Testing with SAM

```bash
# Invoke the function locally
sam local invoke HelloWorldJava -e event.json

# Start a local API Gateway
sam local start-api

# Then call the endpoint
curl http://localhost:3000/lambda-java-hello-world
```

### Testing on AWS

```bash
# Invoke directly
aws lambda invoke \
  --function-name lambda-java-hello-world \
  --payload file://event.json \
  response.json

# View the response
cat response.json

# For API Gateway invocation
curl https://your-api-id.execute-api.us-east-1.amazonaws.com/Prod/lambda-java-hello-world
```

### View Logs

```bash
# Real-time logs
aws logs tail /aws/lambda/lambda-java-hello-world --follow

# Last 10 lines
aws logs tail /aws/lambda/lambda-java-hello-world --max-items 10

# Specific time range
aws logs filter-log-events \
  --log-group-name /aws/lambda/lambda-java-hello-world \
  --start-time $(date -d '-1 hour' +%s)000
```

## Environment Configuration

### Add Environment Variables

In `lambda-cobol-sam.yaml`:

```yaml
Environment:
  Variables:
    LOG_LEVEL: INFO
    CUSTOM_VAR: value
```

Or via CLI:

```bash
aws lambda update-function-configuration \
  --function-name lambda-java-hello-world \
  --environment Variables={LOG_LEVEL=DEBUG,CUSTOM_VAR=value}
```

### Configure Memory and Timeout

```bash
aws lambda update-function-configuration \
  --function-name lambda-java-hello-world \
  --memory-size 512 \
  --timeout 60
```

## Performance Optimization

### Cold Start Optimization

1. **Reduce JAR size**: Use Maven shade with MinimalDeps classifier
2. **Enable Lambda SnapStart**:
   ```bash
   aws lambda update-function-configuration \
     --function-name lambda-java-hello-world \
     --snap-start ApplyOn=PublishedVersions
   ```

3. **Increase memory** (allocates more CPU):
   ```bash
   aws lambda update-function-configuration \
     --function-name lambda-java-hello-world \
     --memory-size 1024
   ```

### Monitoring

```bash
# Get function metrics
aws cloudwatch get-metric-statistics \
  --namespace AWS/Lambda \
  --metric-name Duration \
  --dimensions Name=FunctionName,Value=lambda-java-hello-world \
  --start-time $(date -u -d '-1 hour' +%Y-%m-%dT%H:%M:%S) \
  --end-time $(date -u +%Y-%m-%dT%H:%M:%S) \
  --period 300 \
  --statistics Average,Maximum
```

## Troubleshooting

### Build Fails with Maven

```bash
# Clear Maven cache
rm -rf ~/.m2/repository/com/amazonaws/

# Rebuild with debug info
mvn clean package -X
```

### Function Times Out

- Increase timeout in configuration
- Check CloudWatch Logs for performance issues
- Profile the code with Lambda Insights

### High Memory Usage

- Check for memory leaks
- Review connection pooling configurations
- Consider using Lambda Layers for shared dependencies

### Build Size Issues

Use Maven assembly with exclusions:

```bash
mvn clean package -Dexclusions=**/log4j-config.xml
```

## Cleanup

```bash
# Delete the Lambda function
aws lambda delete-function --function-name lambda-java-hello-world

# Delete IAM role
aws iam detach-role-policy --role-name lambda-java-execution-role \
  --policy-arn arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole

aws iam delete-role --role-name lambda-java-execution-role

# Remove local build artifacts
mvn clean
rm -rf build target
```

## Additional Resources

- [AWS Lambda Java Runtime Documentation](https://docs.aws.amazon.com/lambda/latest/dg/lambda-java.html)
- [AWS SAM CLI Guide](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-cli-command-reference.html)
- [AWS Lambda Best Practices](https://docs.aws.amazon.com/lambda/latest/dg/best-practices.html)
