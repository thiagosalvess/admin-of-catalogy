package com.thiagosalvess.admin.catalogy.domain.video;

import com.thiagosalvess.admin.catalogy.domain.event.DomainEvent;
import com.thiagosalvess.admin.catalogy.domain.utils.InstantUtils;

import java.time.Instant;

public record VideoMediaCreated(
        String resourceId,
        String filePath,
        Instant occurredOn
) implements DomainEvent {

    public VideoMediaCreated(final String resourceId, final String filePath) {
        this(resourceId, filePath, InstantUtils.now());
    }
}
