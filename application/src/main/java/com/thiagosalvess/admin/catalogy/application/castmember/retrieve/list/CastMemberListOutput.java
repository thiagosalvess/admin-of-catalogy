package com.thiagosalvess.admin.catalogy.application.castmember.retrieve.list;

import com.thiagosalvess.admin.catalogy.domain.castmember.CastMember;
import com.thiagosalvess.admin.catalogy.domain.castmember.CastMemberType;

import java.time.Instant;

public record CastMemberListOutput(
        String id,
        String name,
        CastMemberType type,
        Instant createdAt
) {

    public static CastMemberListOutput from(final CastMember aMember) {
        return new CastMemberListOutput(
                aMember.getId().getValue(),
                aMember.getName(),
                aMember.getType(),
                aMember.getCreatedAt()
        );
    }
}
