package com.anunav.aws.snsextendedpoc.controller;

import com.anunav.aws.snsextendedpoc.service.MessagePublishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/sns-publish")
@RequiredArgsConstructor
@Slf4j
public class SNSMessagePublishController {

    private final MessagePublishService messagePublishService;

    @PostMapping
    public void publishMessage(@RequestBody String message) {
        log.info("Received request for SNS Message publish");
        messagePublishService.publishMessage(message);
    }
}
