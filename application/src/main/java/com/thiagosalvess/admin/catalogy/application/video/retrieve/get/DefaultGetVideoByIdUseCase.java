package com.thiagosalvess.admin.catalogy.application.video.retrieve.get;

import com.thiagosalvess.admin.catalogy.domain.exceptions.NotFoundException;
import com.thiagosalvess.admin.catalogy.domain.video.Video;
import com.thiagosalvess.admin.catalogy.domain.video.VideoGateway;
import com.thiagosalvess.admin.catalogy.domain.video.VideoID;

import java.util.Objects;

public class DefaultGetVideoByIdUseCase extends GetVideoByIdUseCase {
    private final VideoGateway videoGateway;

    public DefaultGetVideoByIdUseCase(final VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public VideoOutput execute(final String anIn) {
        final var aVideoId = VideoID.from(anIn);
        return this.videoGateway.findById(aVideoId)
                .map(VideoOutput::from)
                .orElseThrow(() -> NotFoundException.with(Video.class, aVideoId));
    }
}
