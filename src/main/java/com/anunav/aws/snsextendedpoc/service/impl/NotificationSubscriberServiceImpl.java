package com.anunav.aws.snsextendedpoc.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.anunav.aws.snsextendedpoc.dto.S3Details;
import com.anunav.aws.snsextendedpoc.service.NotificationSubscriberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

@Service
@RequiredArgsConstructor
public class NotificationSubscriberServiceImpl implements NotificationSubscriberService {
    private static final String EXTENDED_MESSAGE_DATA = "software.amazon.payloadoffloading.PayloadS3Pointer";

    private final ObjectMapper objectMapper;

    public String getMessage(String message) {
        if (message.contains(EXTENDED_MESSAGE_DATA)) {
            String jsonMessage = message.substring(message.indexOf("{"),
                    message.indexOf("]"));
            S3Details s3Details = getS3Details(jsonMessage);
            if (s3Details != null) {
                return readS3Bucket(s3Details);
            }
        }
        return message;
    }

    private S3Details getS3Details(String message)  {
        try {
            return objectMapper.readValue(message, S3Details.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String readS3Bucket(final S3Details s3Details) {
        final AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .build();

        S3Object object = s3Client.getObject(new GetObjectRequest(s3Details.getS3BucketName(), s3Details.getS3Key()));
        InputStream is = object.getObjectContent();

        try {
            String s3Data =  StreamUtils.copyToString(is, Charset.defaultCharset());
            is.close();
            return s3Data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
