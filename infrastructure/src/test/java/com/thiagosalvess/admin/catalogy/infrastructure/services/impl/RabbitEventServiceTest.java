package com.thiagosalvess.admin.catalogy.infrastructure.services.impl;

import com.thiagosalvess.admin.catalogy.AmqpTest;
import com.thiagosalvess.admin.catalogy.domain.video.VideoMediaCreated;
import com.thiagosalvess.admin.catalogy.infrastructure.configuration.annotations.VideoCreatedQueue;
import com.thiagosalvess.admin.catalogy.infrastructure.configuration.json.Json;
import com.thiagosalvess.admin.catalogy.infrastructure.services.EventService;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@AmqpTest
public class RabbitEventServiceTest {

    private static final String LISTENER = "video.created";

    @Autowired
    @VideoCreatedQueue
    private EventService publisher;

    @Autowired
    private RabbitListenerTestHarness harness;

    @Test
    public void shouldSendMessage() throws InterruptedException {
        final var notification = new VideoMediaCreated("resource", "filepath");

        final var expectedMessage = Json.writeValueAsString(notification);

        this.publisher.send(notification);

        final var invocationData =
                harness.getNextInvocationDataFor(LISTENER, 1, TimeUnit.SECONDS);

        assertNotNull(invocationData);

        assertNotNull(invocationData.getArguments());

        final var actualMessage = (String) invocationData.getArguments()[0];

        assertEquals(expectedMessage, actualMessage);
    }

    @Component
    static class VideoCreatedNewsListener {

        @RabbitListener(id = LISTENER,  queues = "${amqp.queues.video-created.routing-key}")
        void onVideoCreated(@Payload String message) {
            System.out.println(message);
        }
    }
}
