build-HelloWorldJava:
	cp -R . $(ARTIFACTS_DIR)
	cd $(ARTIFACTS_DIR) && \
	mvn clean package -DskipTests=true && \
	mkdir -p bootstrap-artifacts && \
	cp pom.xml bootstrap-artifacts/ && \
	cp -r src bootstrap-artifacts/ && \
	cp bootstrap bootstrap-artifacts/ && \
	cp target/lambda-java-hello.jar bootstrap-artifacts/