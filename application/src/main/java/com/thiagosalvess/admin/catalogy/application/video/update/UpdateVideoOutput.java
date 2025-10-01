package com.thiagosalvess.admin.catalogy.application.video.update;

import com.thiagosalvess.admin.catalogy.domain.video.Video;

public record UpdateVideoOutput(String id) {

    public static UpdateVideoOutput from(final Video aVideo) {
        return new UpdateVideoOutput(aVideo.getId().getValue());
    }
}
