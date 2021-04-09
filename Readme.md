# Getting Started

### Introduction
In general a SNS message cannot exceed 256 KB. If the message size needs to be more than 256 KB then https://github.com/awslabs/amazon-sns-java-extended-client-lib/ could be used to send message.

This project is for learning
* SNS Extended client
* SQS Extended Client to read SNS Notifications and
* REST HTTP endpoint to read the data.

### Set up before running
For this app to work we need some basic setup

#### Configure AWS SNS Topic
Create SNS Topic by logging into AWS Console. (Default options should work fine for this example)

Copy the ARN name for the topic and update it in application.properties

Update the defaultRegion and S3 Bucket details if required based on your AWS setup.

### How to run

Compile the project in local environment
> mvn clean install

Copy the artifact to EC2 instance. 
> scp -i "~/Downloads/aws-ec2-rest-test.pem" target/snsextendedpoc-0.0.1-SNAPSHOT.jar ec2-user@ec2-54-184-120-89.us-west-2.compute.amazonaws.com:/home/ec2-user

Login to EC2 instance and run the jar
> ssh -i "~/Downloads/aws-ec2-rest-test.pem" ec2-user@ec2-54-184-120-89.us-west-2.compute.amazonaws.com

> java -jar snsextendedpoc-0.0.1-SNAPSHOT.jar

NOTE: If the application fails to startup complaning about AWS Secrets then add the AWS Credentials as variable 
> -DAWS_ACCESS_KEY={your_aws_key} -DAWS_SECRET_KEY={your_aws_secret}

### How to subscribe
Once the application is up & running.
1. Login in to AWS Console
2. Access the SNS Topic created and click on the `Create subscription`
3. For Protocol select `http` and in endpoint give the `{your_ec2_instance_domain}/sns-topic-subscriber`. 
   Leave the `Enable raw message delivery` option as Unchecked.
4. `Create Subscription`
    If the service is up and running - the Subscription would be confirmed.
   
   
### How to test
#### Send a POST request with a sample body that is less than 1000 Bytes

> curl --location --request POST 'http://ec2-54-184-120-89.us-west-2.compute.amazonaws.com:8080/sns-publish' \
--header 'Content-Type: text/plain' \
--data-raw '"This is a simple message."'

#### Now Post a long message

> curl --location --request POST 'http://ec2-54-184-120-89.us-west-2.compute.amazonaws.com:8080/sns-publish' \
--header 'Content-Type: text/plain' \
--data-raw '"This is a long message with lots of text to make the size greater than 1000 bytes.
This will make the SNS extended client to use to S3 Bucket
What is Lorem Ipsum?
Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry'\''s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.
It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using '\''Content here, content here'\'', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for '\''lorem ipsum'\'' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like)."'

With long message the SNS Message would be
>  ["software.amazon.payloadoffloading.PayloadS3Pointer",{"s3BucketName":"sns-extended-client-poc-bucket","s3Key":"abb60469-9f88-40bd-9596-4e8a9f8e3def"}]
