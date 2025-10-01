package com.thiagosalvess.admin.catalogy.application.video.create;

import com.thiagosalvess.admin.catalogy.domain.video.Video;

public record CreateVideoOutput(String id) {

    public static CreateVideoOutput from(final Video aVideo) {
        return new CreateVideoOutput(aVideo.getId().getValue());
    }
}
