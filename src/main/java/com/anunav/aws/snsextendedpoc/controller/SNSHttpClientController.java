package com.anunav.aws.snsextendedpoc.controller;

import com.anunav.aws.snsextendedpoc.service.NotificationSubscriberService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.aws.messaging.config.annotation.NotificationMessage;
import org.springframework.cloud.aws.messaging.config.annotation.NotificationSubject;
import org.springframework.cloud.aws.messaging.endpoint.NotificationStatus;
import org.springframework.cloud.aws.messaging.endpoint.annotation.NotificationMessageMapping;
import org.springframework.cloud.aws.messaging.endpoint.annotation.NotificationSubscriptionMapping;
import org.springframework.cloud.aws.messaging.endpoint.annotation.NotificationUnsubscribeConfirmationMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sns-topic-subscriber")
@RequiredArgsConstructor
public class SNSHttpClientController {

    private static final Logger logger = LoggerFactory.getLogger(SNSHttpClientController.class);

    private final NotificationSubscriberService notificationSubscriberService;

    @NotificationMessageMapping
    public void receiveNotification(@NotificationMessage String message, @NotificationSubject String subject) {
        logger.info("Received message: {}, having subject: {}", message, subject);
        logger.info("Final message: {}", notificationSubscriberService.getMessage(message));
    }

    @NotificationUnsubscribeConfirmationMapping
    public void confirmSubscriptionMessage(NotificationStatus notificationStatus) {
        logger.info("Unsubscribed from Topic");
        notificationStatus.confirmSubscription();
    }

    @NotificationSubscriptionMapping
    public void confirmUnsubscribeMessage(NotificationStatus notificationStatus) {
        logger.info("Subscribed to Topic");
        notificationStatus.confirmSubscription();
    }
}
