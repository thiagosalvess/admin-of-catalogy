package com.thiagosalvess.admin.catalogy.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record VideoMessage(
        @JsonProperty("resource_id") String resourceId,
        @JsonProperty("file_path") String filePath
) {
}
