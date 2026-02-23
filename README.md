<img src="https://github.com/didier-durand/lambda-cobol/blob/main/img/aws-lambda.png" height="110"> <img src="https://github.com/didier-durand/lambda-cobol/blob/main/img/cobol-logo.jpeg" height="110">

# Legacy Serverless Modernization: From Cobol to Java 21 on AWS Lambda

![Java 21 Lambda](https://img.shields.io/badge/Java-21-blue) ![AWS Lambda](https://img.shields.io/badge/AWS-Lambda-orange)

## 🎯 Current Status

This repository has been **converted from COBOL to Java 21**. It demonstrates how to modernize legacy serverless applications while maintaining the original functionality.

### What Changed
- ✅ Language: COBOL → Java 21
- ✅ Runtime: GnuCOBOL custom runtime → AWS managed Java 21 runtime
- ✅ Build: Direct compilation → Maven 3.6.3+
- ✅ Added: Full unit tests, structured logging, proper error handling

### Documentation
- **[README-JAVA.md](README-JAVA.md)** - Complete Java 21 implementation guide
- **[BUILD_GUIDE.md](BUILD_GUIDE.md)** - Detailed build and deployment instructions  
- **[CONVERSION_SUMMARY.md](CONVERSION_SUMMARY.md)** - Technical conversion details

---

## Table of Contents

* [Quick Start](#quick-start)
* [Goal](#goal)
* [Java 21 Implementation](#java-21-implementation)
* [Lambda Functions and Serverless Architecture](README.md#lambda-functions-and-serverless-architecture)
* [Serverless Application Model](README.md#serverless-application-model)
* [Original COBOL Project](#original-cobol-project)

## 🚀 Quick Start

### Build
```bash
mvn clean package
```

### Test
```bash
mvn test
```

### Deploy
```bash
sam build
sam deploy --guided
```

### Invoke
```bash
aws lambda invoke --function-name lambda-java-hello-world response.json
cat response.json
```

## Goal

This repository originally demonstrated running a [Cobol program](hello-world.cob) as an [AWS Lambda function](https://aws.amazon.com/lambda/) 
using [GnuCOBOL](https://en.wikipedia.org/wiki/GnuCOBOL) with a custom Lambda runtime. 

**The project has now been modernized** to Java 21, showcasing:
1. **Legacy Modernization** - Converting older applications to modern languages
2. **Serverless Architecture** - Leveraging AWS Lambda for scalability
3. **Best Practices** - Proper handler patterns, testing, logging, and deployment
4. **Cost Optimization** - Reduced image size, managed runtime, pay-per-use model

The benefits of the serverless architecture are not reserved to newly written 
applications. The purpose of this showcase is to demonstrate how those benefits can be combined with legacy code, still "doing the job" and 
delivering solid business value, to further extend its life.

**Original Note**: The deployed program is accessible over http via the definition of a REST service on the [AWS API gateway](https://aws.amazon.com/api-gateway/). 

## Java 21 Implementation

### Original COBOL Program
```cobol
IDENTIFICATION DIVISION.
PROGRAM-ID. hello.
PROCEDURE DIVISION.
DISPLAY "Hello World from COBOL!".
STOP RUN.
```

### Java 21 Equivalent
```java
public class HelloWorldHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("Hello World from Java 21!");

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setStatusCode(200);
        response.setHeaders(Map.of("Content-Type", "application/json"));
        response.setBody("{\n  \"message\": \"Hello World from Java 21!\",\n  \"runtime\": \"Java 21\"\n}");
        
        return response;
    }
}
```

### Why Java 21?
- **Type Safety** - Compile-time error detection
- **Performance** - JIT compilation and optimization
- **Ecosystem** - Access to 1M+ Maven packages
- **Modern Features** - Virtual threads, records, pattern matching
- **Testability** - Full JUnit 5 test framework
- **Maintainability** - Industry-standard language with broad expertise
- **Cost** - Smaller Docker images, managed runtime, pay-per-use

### Project Structure
```
src/main/java/com/lambda/
└── HelloWorldHandler.java          # Lambda handler

src/test/java/com/lambda/
└── HelloWorldHandlerTest.java      # Unit tests

pom.xml                             # Maven configuration
Dockerfile                          # Java 21 container image
bootstrap                           # Lambda runtime bootstrap
lambda-cobol-sam.yaml               # AWS SAM template
```

### Key Metrics

| Metric | COBOL | Java 21 |
|--------|-------|---------|
| **Docker Image Size** | ~800MB | ~400-500MB |
| **Build Time** | ~5 min | ~2 min |
| **Cold Start** | ~500ms | ~1-2s |
| **Testability** | None | JUnit 5 |
| **Maintenance** | Difficult | High |



This initial use case will be refined in upcoming versions by adding a database, calling subprograms, etc.

## Original COBOL Project

The original implementation demonstrated deploying a Cobol program compiled with [GnuCOBOL](https://en.wikipedia.org/wiki/GnuCOBOL) 
as an AWS Lambda function using a custom runtime. While that implementation worked well, modernizing to Java 21 provides:
- Better long-term maintainability
- Access to modern tooling and libraries  
- Reduced operational complexity
- Cost optimization through managed runtimes

### Historical Context: Cobol in 2024

[Cobol](https://en.wikipedia.org/wiki/COBOL) was specified more than 60 years ago by [Grace Hopper](https://en.wikipedia.org/wiki/Grace_Hopper). 
Despite its age, it remains critical infrastructure:

- **200+ billion** lines of Cobol still in production
- **43%** of banking systems built on Cobol
- **95%** of ATM swipes rely on Cobol
- **5 billion** additional lines produced annually

The modernization of this Cobol Lambda example to Java 21 shows how legacy systems can be evolved to modern platforms while maintaining value.

## Lambda Functions and Serverless Architecture

<p align="center">
<img src="https://github.com/didier-durand/lambda-cobol/blob/main/img/lambda-arch.jpeg" height="320">
</p>
<p align="center">
<b>Canonical Lambda architecture (Python example)</b>
</p>

As per AWS documentation: *"AWS Lambda is a serverless compute service that lets you run code without provisioning or managing servers, 
creating workload-aware cluster scaling logic, maintaining event integrations, or managing runtimes. With Lambda, you can run code for virtually 
any type of application or backend service - all with zero administration. Just upload your code as a ZIP file or container image, and 
Lambda automatically and precisely allocates compute execution power and runs your code based on the incoming request or event, for any scale of 
traffic."*

It means that AWS Lambda service does the heavyweight lifting to the benefit of its users: all the [non-functional requirements (NFRs)](https://en.wikipedia.org/wiki/Non-functional_requirement) 
like high availability, scalability, security, resource and performance optimization, etc. are implemented by the AWS experts. Customer teams can 
remain focused on functional code and rely on this scalable platform to run their applications uninterruptedly at scale.

This serverless architecture is the optimal solution to run applications with very stringent [SLAs](https://en.wikipedia.org/wiki/Service-level_agreement) 
(incl. extremely low [RTO](https://en.wikipedia.org/wiki/Disaster_recovery#Recovery_Time_Objective) & [RPO](https://en.wikipedia.org/wiki/Disaster_recovery#Recovery_Point_Objective)). 
Those highly demanding SLAs are usually out of reach of most organizations by themselves. With Lambdas, it become extremely easy as AWS encapsulates 
its domain expertise in the underlying platform design and makes the thorough investments to deploy and operate the underlying infrastructure 
in a multi-tenant fashion. 

Additionally, the service strictly respects the "pay-per-use" model: AWS will charge only for the memory consumption and 
execution time of executed Lambdas. If they remain unused, no cost!

These virtues of serverless architecture make it the ideal target when the functional needs and technical requirements allow for it. It should be the 
primary goal of any corporate cloud architect to make maximum use of managed serverless features.

## Serverless Application Model

AWS open sourced at end of 2016 the [Serverless Application Model (SAM) framework](https://github.com/aws/serverless-application-model) to [describe 
such serverless applications](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/what-is-sam.html) made of multiple 
Lamdba functions with all their dependencies. The main purpose of SAM is to reduce the effort by developers when creating such applications. 
Required artefacts and definitions are specified at a high-level of abstraction. The SAM processor on AWS, in collaboration with the 
[CloudFormation service](https://aws.amazon.com/cloudformation/), which it extends, will take care of all the low-level definitions of corresponding required AWS resources to deploy the Lambda function and make it 
publicly accessible through the API gateway. 

This allows the implementation of [Infrastructure-as-Code](https://en.wikipedia.org/wiki/Infrastructure_as_code) best practices.

The SAM model for our Cobol Lambda is in the file [lambda-cobol-sam.yaml](lambda-cobol-sam.yaml). It contains a single [AWS::Serverless::Function](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/sam-resource-function.html) with its parameters. During deployment, SAM & CloudFormation expand it into 6 more granular resources that can be located with "Resource creation Initiated" in last section below.

## Workflow and Components

Implemented as a Github Action, the workflow  - scripted in [lambda-cobol.sh](lambda-cobol.sh) - comprises following key steps:

1) A Docker image [is constructed](Dockerfile) to install the GnuCOBOL compiler and its dependencies on top of the base Amazon Linux image. The 
purpose of such a container is to leverage the isolation provided by containers. Consequently, the build environment is fully controlled.
2) This Cobol builder imports the source code of [hello-world.cob](hello-world.cob) and compiles it to generate an x86 native binary named ```hello-world```.
3) This binary is packaged, via SAM CLI, with other required runtime artefacts. The libcob library is required by GnuCOBOL. The shell script ```bootstrap```(name 
imposed by specifications) implements the requirements of [custom Lambda runtimes](https://docs.aws.amazon.com/lambda/latest/dg/runtimes-walkthrough.html).
4) This package is deployed on the Lambda service via [SAM CLI](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-cli-install.html).
5) The SAM description is processed by AWS Lambda and CloudFormation to deploy the function.
6) SAM CLI is used to check proper deployment.
7) SAM CLI invokes the function synchronously.
8) curl calls the URL with the obtained DNS to validate the proper execution of the newly deployed Lambda. The URL for curl is built   following template: https://$API_ID.execute-api.$AWS_REGION.amazonaws.com/Prod/$LAMBDA_NAME. $API_ID is dynamic and obtained via a ```aws apigateway get-rest-apis``` CLI command.

When a previous deployment of the CloudFormation stack is active, it gets deleted just before the SAM build to trigger the entire CloudFormation deployment process.

**Note:** the version of GnuCOBOL currently used is v2.2. A [version 3.1 was published](https://sourceforge.net/projects/gnucobol/files/gnucobol/) in late December, 2020. But, its libcob runtime library has hardwired dependencies on very recent Linux system libraries, that are not yet available with proper version in Lambda runtime. We'll bump to newest GnuCOBOL when Lambda runtime gets updated. The official repository for all available versions of GnuCOBOL is [here]( https://ftp.gnu.org/gnu/gnucobol/?C=M;O=D).

## Fork and Setup  

When you fork this repository to run it on your own, you will need to recreate three [Github secrets](https://docs.github.com/en/actions/configuring-and-managing-workflows/using-variables-and-secrets-in-a-workflow) in your own repository for workflows to work properly: 

- ${{ secrets.AWS_ACCESS_KEY_ID }}: the access key under which the workflow will run
- ${{ secrets.AWS_SECRET_ACCESS_KEY }}: the secret key validating the use of the above access key
- ${{ secrets.AWS_REGION }}: the region in which you want the workflow to be deployed and executed

The credentials given to this identity via [AWS IAM](https://aws.amazon.com/iam/) must grant permissions to deploy and run Lambda functions as well 
as create corresponding API gateway definitions. In addition, an S3 bucket must be created to import the uploaded artefacts in AWS when they get deployed.

## Execution Highlights

Below are the logs of the last execution related to the Lamdba service operated from SAM CLI:



```
 
### execution date: Thu Mar  9 01:33:05 UTC 2023
 
### Check existing Lambdas functions...
{
    "Functions": [
        {
            "FunctionName": "Hello-world-Python",
            "FunctionArn": "arn:aws:lambda:us-east-1:514764745669:function:Hello-world-Python",
            "Runtime": "python3.8",
            "Role": "arn:aws:iam::514764745669:role/service-role/Hello-world-Python-role-lyqky200",
            "Handler": "lambda_function.lambda_handler",
            "CodeSize": 299,
            "Description": "",
            "Timeout": 3,
            "MemorySize": 128,
            "LastModified": "2021-02-06T10:48:38.267+0000",
            "CodeSha256": "fI06ZlRH/KN6Ra3twvdRllUYaxv182Tjx0qNWNlKIhI=",
            "Version": "$LATEST",
            "TracingConfig": {
                "Mode": "PassThrough"
            },
            "RevisionId": "d90d1b6d-667c-46d9-b9d5-e7fdefdfc004",
            "PackageType": "Zip",
            "Architectures": [
                "x86_64"
            ],
            "EphemeralStorage": {
                "Size": 512
            },
            "SnapStart": {
                "ApplyOn": "None",
                "OptimizationStatus": "Off"
            }
        }
    ]
}
 
### Starting SAM build...

Build Succeeded

Built Artifacts  : build
Built Template   : build/template.yaml

Commands you can use next
=========================
[*] Validate SAM template: sam validate
[*] Invoke Function: sam local invoke -t build/template.yaml
[*] Test Function in the Cloud: sam sync --stack-name {{stack-name}} --watch
[*] Deploy: sam deploy --guided --template-file build/template.yaml
 
### Starting SAM deployment...

	Deploying with following values
	===============================
	Stack name                   : lambda-cobol-stack
	Region                       : us-east-1
	Confirm changeset            : False
	Disable rollback             : False
	Deployment s3 bucket         : net.didier-durand.lambda-code
	Capabilities                 : ["CAPABILITY_IAM"]
	Parameter overrides          : {}
	Signing Profiles             : {}

Initiating deployment
=====================

Waiting for changeset to be created..
CloudFormation stack changeset
-------------------------------------------------------------------------------------------------
Operation                LogicalResourceId        ResourceType             Replacement            
-------------------------------------------------------------------------------------------------
+ Add                    HelloWorldCobolGetReso   AWS::Lambda::Permissio   N/A                    
                         urcePermissionProd       n                                               
+ Add                    HelloWorldCobolRole      AWS::IAM::Role           N/A                    
+ Add                    HelloWorldCobol          AWS::Lambda::Function    N/A                    
+ Add                    ServerlessRestApiDeplo   AWS::ApiGateway::Deplo   N/A                    
                         ymentaf1c952223          yment                                           
+ Add                    ServerlessRestApiProdS   AWS::ApiGateway::Stage   N/A                    
                         tage                                                                     
+ Add                    ServerlessRestApi        AWS::ApiGateway::RestA   N/A                    
                                                  pi                                              
-------------------------------------------------------------------------------------------------

Changeset created successfully. arn:aws:cloudformation:us-east-1:514764745669:changeSet/samcli-deploy1678325694/7f9ee7f0-fc45-4a76-a364-87bc073b74ec


2023-03-09 01:35:06 - Waiting for stack create/update to complete

CloudFormation events from stack operations (refresh every 0.5 seconds)
-------------------------------------------------------------------------------------------------
ResourceStatus           ResourceType             LogicalResourceId        ResourceStatusReason   
-------------------------------------------------------------------------------------------------
CREATE_IN_PROGRESS       AWS::IAM::Role           HelloWorldCobolRole      -                      
CREATE_IN_PROGRESS       AWS::IAM::Role           HelloWorldCobolRole      Resource creation      
                                                                           Initiated              
CREATE_COMPLETE          AWS::IAM::Role           HelloWorldCobolRole      -                      
CREATE_IN_PROGRESS       AWS::Lambda::Function    HelloWorldCobol          -                      
CREATE_IN_PROGRESS       AWS::Lambda::Function    HelloWorldCobol          Resource creation      
                                                                           Initiated              
CREATE_COMPLETE          AWS::Lambda::Function    HelloWorldCobol          -                      
CREATE_IN_PROGRESS       AWS::ApiGateway::RestA   ServerlessRestApi        -                      
                         pi                                                                       
CREATE_IN_PROGRESS       AWS::ApiGateway::RestA   ServerlessRestApi        Resource creation      
                         pi                                                Initiated              
CREATE_COMPLETE          AWS::ApiGateway::RestA   ServerlessRestApi        -                      
                         pi                                                                       
CREATE_IN_PROGRESS       AWS::ApiGateway::Deplo   ServerlessRestApiDeplo   -                      
                         yment                    ymentaf1c952223                                 
CREATE_IN_PROGRESS       AWS::Lambda::Permissio   HelloWorldCobolGetReso   -                      
                         n                        urcePermissionProd                              
CREATE_IN_PROGRESS       AWS::Lambda::Permissio   HelloWorldCobolGetReso   Resource creation      
                         n                        urcePermissionProd       Initiated              
CREATE_IN_PROGRESS       AWS::ApiGateway::Deplo   ServerlessRestApiDeplo   Resource creation      
                         yment                    ymentaf1c952223          Initiated              
CREATE_COMPLETE          AWS::ApiGateway::Deplo   ServerlessRestApiDeplo   -                      
                         yment                    ymentaf1c952223                                 
CREATE_IN_PROGRESS       AWS::ApiGateway::Stage   ServerlessRestApiProdS   -                      
                                                  tage                                            
CREATE_IN_PROGRESS       AWS::ApiGateway::Stage   ServerlessRestApiProdS   Resource creation      
                                                  tage                     Initiated              
CREATE_COMPLETE          AWS::ApiGateway::Stage   ServerlessRestApiProdS   -                      
                                                  tage                                            
CREATE_COMPLETE          AWS::Lambda::Permissio   HelloWorldCobolGetReso   -                      
                         n                        urcePermissionProd                              
CREATE_COMPLETE          AWS::CloudFormation::S   lambda-cobol-stack       -                      
                         tack                                                                     
-------------------------------------------------------------------------------------------------

Successfully created/updated stack - lambda-cobol-stack in us-east-1

 
 
### Inkoking deployed Lambda synchronously from CLI...
{
    "StatusCode": 200,
    "ExecutedVersion": "$LATEST"
}
invocation result:

  {
    "isBase64Encoded": false,
    "statusCode": 200, 
    "body": "Hello World from COBOL!" 
  } 
 
### Obtaining API gateway config...
{
    "items": [
        {
            "id": "eioy58x8l3",
            "name": "lambda-cobol-stack",
            "createdDate": "2023-03-09T01:36:00+00:00",
            "version": "1.0",
            "apiKeySource": "HEADER",
            "endpointConfiguration": {
                "types": [
                    "EDGE"
                ]
            },
            "tags": {
                "aws:cloudformation:logical-id": "ServerlessRestApi",
                "aws:cloudformation:stack-id": "arn:aws:cloudformation:us-east-1:514764745669:stack/lambda-cobol-stack/9843f110-be1a-11ed-a5ac-0e778226314b",
                "aws:cloudformation:stack-name": "lambda-cobol-stack"
            },
            "disableExecuteApiEndpoint": false
        }
    ]
}
api id: eioy58x8l3
 
### Running curl https request to https://eioy58x8l3.execute-api.us-east-1.amazonaws.com/Prod/lambda-cobol-hello-world ...
Hello World from COBOL! 
```
