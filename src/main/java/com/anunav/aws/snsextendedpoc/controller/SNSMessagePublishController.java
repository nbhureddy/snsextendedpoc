package com.anunav.aws.snsextendedpoc.controller;

import com.anunav.aws.snsextendedpoc.service.MessagePublishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller("/sns-publish")
@RequiredArgsConstructor
@Slf4j
public class SNSMessagePublishController {

    private final MessagePublishService messagePublishService;

    @PostMapping
    public void publishMessage(@RequestBody final String message) {
        log.info("Received request for SNS Message publish");
        messagePublishService.publishMessage(message);
    }
}
