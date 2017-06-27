# README #

### What is this repository for? ###

Support for triggering step function execution in response to various AWS events.

### How do I get set up? ###

* Compile with maven -> mvn package

### What support does it offer? ###

* ``27-06-2017`` - Support for S3 events. Create a Lambda function using this artifact and add an environment variable named `JAVA_TOOL_OPTIONS` and having value `-Daws.accessKeyId=<> -Daws.secretKey=<> -DstateMachineARN=<> -DawsRegion=<>`
