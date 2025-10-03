package com.thiagosalvess.admin.catalogy.infrastructure.amqp;

import com.thiagosalvess.admin.catalogy.AmqpTest;
import com.thiagosalvess.admin.catalogy.application.video.media.update.UpdateMediaStatusCommand;
import com.thiagosalvess.admin.catalogy.application.video.media.update.UpdateMediaStatusUseCase;
import com.thiagosalvess.admin.catalogy.domain.utils.IdUtils;
import com.thiagosalvess.admin.catalogy.domain.video.MediaStatus;
import com.thiagosalvess.admin.catalogy.infrastructure.configuration.annotations.VideoEncodedQueue;
import com.thiagosalvess.admin.catalogy.infrastructure.configuration.json.Json;
import com.thiagosalvess.admin.catalogy.infrastructure.configuration.properties.amqp.QueueProperties;
import com.thiagosalvess.admin.catalogy.infrastructure.video.models.VideoEncoderCompleted;
import com.thiagosalvess.admin.catalogy.infrastructure.video.models.VideoEncoderError;
import com.thiagosalvess.admin.catalogy.infrastructure.video.models.VideoMessage;
import com.thiagosalvess.admin.catalogy.infrastructure.video.models.VideoMetadata;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.amqp.rabbit.test.TestRabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@AmqpTest
public class VideoEncoderListenerTest {

    @Autowired
    private TestRabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitListenerTestHarness harness;

    @MockBean
    private UpdateMediaStatusUseCase updateMediaStatusUseCase;

    @Autowired
    @VideoEncodedQueue
    private QueueProperties queueProperties;

    @Test
    public void givenErrorResult_whenCallsListener_shouldProcess() throws InterruptedException {
        final var expectedError = new VideoEncoderError(
                new VideoMessage("123", "abc"),
                "Video not found"
        );

        final var expectedMessage = Json.writeValueAsString(expectedError);

        this.rabbitTemplate.convertAndSend(queueProperties.getQueue(), expectedMessage);

        final var invocationData =
                harness.getNextInvocationDataFor(VideoEncoderListener.LISTENER_ID, 1, TimeUnit.SECONDS);

        assertNotNull(invocationData);
        assertNotNull(invocationData.getArguments());

        final var actualMessage = (String) invocationData.getArguments()[0];
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void givenCompletedResult_whenCallsListener_shouldCallUseCase() throws InterruptedException {
        final var expectedId = IdUtils.uuid();
        final var expectedOutputBucket = "codeeducationtest";
        final var expectedStatus = MediaStatus.COMPLETED;
        final var expectedEncoderVideoFolder = "anyfolder";
        final var expectedResourceId = IdUtils.uuid();
        final var expectedFilePath = "any.mp4";
        final var expectedMetadata =
                new VideoMetadata(expectedEncoderVideoFolder, expectedResourceId, expectedFilePath);

        final var aResult = new VideoEncoderCompleted(expectedId, expectedOutputBucket, expectedMetadata);

        final var expectedMessage = Json.writeValueAsString(aResult);

        doNothing().when(updateMediaStatusUseCase).execute(any());

        this.rabbitTemplate.convertAndSend(queueProperties.getQueue(), expectedMessage);

        final var invocationData =
                harness.getNextInvocationDataFor(VideoEncoderListener.LISTENER_ID, 1, TimeUnit.SECONDS);

        assertNotNull(invocationData);
        assertNotNull(invocationData.getArguments());

        final var actualMessage = (String) invocationData.getArguments()[0];
        assertEquals(expectedMessage, actualMessage);

        final var cmdCaptor = ArgumentCaptor.forClass(UpdateMediaStatusCommand.class);
        verify(updateMediaStatusUseCase).execute(cmdCaptor.capture());

        final var actualCommand = cmdCaptor.getValue();
        assertEquals(expectedStatus, actualCommand.status());
        assertEquals(expectedId, actualCommand.videoId());
        assertEquals(expectedResourceId, actualCommand.resourceId());
        assertEquals(expectedEncoderVideoFolder, actualCommand.folder());
        assertEquals(expectedFilePath, actualCommand.filename());
    }
}
