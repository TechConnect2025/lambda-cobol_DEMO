FROM public.ecr.aws/amazonlinux/amazonlinux:latest

# Install Java 21 and Maven
RUN yum update -y && \
    yum install -y java-21-amazon-corretto-devel maven && \
    yum clean all

# Set JAVA_HOME
ENV JAVA_HOME=/usr/lib/jvm/java-21-amazon-corretto
ENV PATH=$JAVA_HOME/bin:$PATH

WORKDIR /app

# Copy the Maven pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the application with Maven
RUN mvn clean package

# Create runtime directory
WORKDIR /var/task
RUN mkdir -p lib
RUN cp /app/target/lambda-java-hello.jar .

# Copy bootstrap script for custom runtime
COPY bootstrap .
RUN chmod +x bootstrap

# Set the working directory
WORKDIR /var/task
