package com.thiagosalvess.admin.catalogy.application.video.media.upload;

import com.thiagosalvess.admin.catalogy.domain.video.VideoResource;

public record UploadMediaCommand(
        String videoId,
        VideoResource videoResource
) {

    public static UploadMediaCommand with(final String anId, final VideoResource aResource) {
        return new UploadMediaCommand(anId, aResource);
    }
}
