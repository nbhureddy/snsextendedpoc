package com.anunav.aws.snsextendedpoc.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.sns.AmazonSNS;
import com.anunav.aws.snsextendedpoc.service.MessagePublishService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.sns.AmazonSNSExtendedClient;
import software.amazon.sns.SNSExtendedClientConfiguration;

@Service
@RequiredArgsConstructor
public class MessagePublishServiceImpl implements MessagePublishService {

    @Value("aws.default-region")
    private String defaultRegion;

    @Value("aws.s3.bucket-name")
    private String BUCKET_NAME;

    @Value("aws.sns.topic-arn")
    private String TOPIC_ARN;

    @Value("aws.sns.message.threshold")
    private Integer EXTENDED_STORAGE_MESSAGE_SIZE_THRESHOLD;

    private final AmazonSNS amazonSNS;
    private final AmazonS3 amazonS3;

    @Override
    public void publishMessage(final String message) {
        //Create Bucket. Check before creation as this will throw an error if the bucket already exists
        if (! amazonS3.doesBucketExistV2(BUCKET_NAME)) {
            amazonS3.createBucket(BUCKET_NAME);
        }

        //To read message content stored in S3 transparently through SQS extended client,
        //set the RawMessageDelivery subscription attribute to TRUE
//        final SetSubscriptionAttributesRequest subscriptionAttributesRequest = new SetSubscriptionAttributesRequest();
//        subscriptionAttributesRequest.setSubscriptionArn(subscriptionArn);
//        subscriptionAttributesRequest.setAttributeName("RawMessageDelivery");
//        subscriptionAttributesRequest.setAttributeValue("TRUE");
//        snsClient.setSubscriptionAttributes(subscriptionAttributesRequest);

        //Initialize SNS extended client
        //PayloadSizeThreshold triggers message content storage in S3 when the threshold is exceeded
        //To store all messages content in S3, use AlwaysThroughS3 flag
        final SNSExtendedClientConfiguration snsExtendedClientConfiguration = new SNSExtendedClientConfiguration()
                .withPayloadSupportEnabled(amazonS3, BUCKET_NAME)
                .withPayloadSizeThreshold(EXTENDED_STORAGE_MESSAGE_SIZE_THRESHOLD);

        final AmazonSNSExtendedClient snsExtendedClient = new AmazonSNSExtendedClient(amazonSNS, snsExtendedClientConfiguration);

        //Publish message via SNS with storage in S3
        snsExtendedClient.publish(TOPIC_ARN, message);
    }

}
