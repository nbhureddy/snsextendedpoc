package com.anunav.aws.snsextendedpoc.config;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSConfig {

    @Bean
    public AmazonSNS getAmazonSNS() {
        return AmazonSNSClientBuilder.standard()
                .build();
    }

    @Bean
    public AmazonS3 getAmazonS3() {
        return AmazonS3ClientBuilder.standard()
                .build();
    }
}
