package com.anunav.aws.snsextendedpoc.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.sns.AmazonSNS;
import com.anunav.aws.snsextendedpoc.service.MessagePublishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.sns.AmazonSNSExtendedClient;
import software.amazon.sns.SNSExtendedClientConfiguration;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessagePublishServiceImpl implements MessagePublishService {

    @Value("${aws.s3.bucket-name}")
    private String BUCKET_NAME;

    @Value("${aws.sns.topic-arn}")
    private String TOPIC_ARN;

    @Value("${aws.sns.message.threshold}")
    private String EXTENDED_STORAGE_MESSAGE_SIZE_THRESHOLD;

    private final AmazonSNS amazonSNS;
    private final AmazonS3 amazonS3;

    @Override
    public void publishMessage(final String message) {
        //Create Bucket. Check before creation as this will throw an error if the bucket already exists
        if (! amazonS3.doesBucketExistV2(BUCKET_NAME)) {
            log.info("S3 Bucket does not exist. So creating S3 Bucket with name {}", BUCKET_NAME);
            amazonS3.createBucket(BUCKET_NAME);
        }

        final SNSExtendedClientConfiguration snsExtendedClientConfiguration = new SNSExtendedClientConfiguration()
                .withPayloadSupportEnabled(amazonS3, BUCKET_NAME)
                .withPayloadSizeThreshold(Integer.parseInt(EXTENDED_STORAGE_MESSAGE_SIZE_THRESHOLD));

        final AmazonSNSExtendedClient snsExtendedClient = new AmazonSNSExtendedClient(amazonSNS, snsExtendedClientConfiguration);

        //Publish message via SNS with storage in S3
        snsExtendedClient.publish(TOPIC_ARN, message);
    }

}
