package com.thiagosalvess.admin.catalogy.application.video.media.upload;

import com.thiagosalvess.admin.catalogy.domain.video.Video;
import com.thiagosalvess.admin.catalogy.domain.video.VideoMediaType;

public record UploadMediaOutput(
        String videoId,
        VideoMediaType mediaType
) {

    public static UploadMediaOutput with(final Video aVideo, final VideoMediaType aType) {
        return new UploadMediaOutput(aVideo.getId().getValue(), aType);
    }
}
