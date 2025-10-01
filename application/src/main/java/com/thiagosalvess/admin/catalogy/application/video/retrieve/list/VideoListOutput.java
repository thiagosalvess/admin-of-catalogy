package com.thiagosalvess.admin.catalogy.application.video.retrieve.list;

import com.thiagosalvess.admin.catalogy.domain.video.Video;
import com.thiagosalvess.admin.catalogy.domain.video.VideoPreview;

import java.time.Instant;

public record VideoListOutput(String id,
                              String title,
                              String description,
                              Instant createdAt,
                              Instant updatedAt
) {

    public static VideoListOutput from(final Video aVideo) {
        return new VideoListOutput(
                aVideo.getId().getValue(),
                aVideo.getTitle(),
                aVideo.getDescription(),
                aVideo.getCreatedAt(),
                aVideo.getUpdatedAt()
        );
    }

    public static VideoListOutput from(final VideoPreview aVideo) {
        return new VideoListOutput(
                aVideo.id(),
                aVideo.title(),
                aVideo.description(),
                aVideo.createdAt(),
                aVideo.updatedAt()
        );
    }
}
