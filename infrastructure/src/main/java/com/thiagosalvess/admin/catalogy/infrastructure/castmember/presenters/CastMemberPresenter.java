package com.thiagosalvess.admin.catalogy.infrastructure.castmember.presenters;

import com.thiagosalvess.admin.catalogy.application.castmember.retrieve.get.CastMemberOutput;
import com.thiagosalvess.admin.catalogy.application.castmember.retrieve.list.CastMemberListOutput;
import com.thiagosalvess.admin.catalogy.infrastructure.castmember.models.CastMemberListResponse;
import com.thiagosalvess.admin.catalogy.infrastructure.castmember.models.CastMemberResponse;

public interface CastMemberPresenter {
    static CastMemberResponse present(final CastMemberOutput aMember) {
        return new CastMemberResponse(
                aMember.id(),
                aMember.name(),
                aMember.type().name(),
                aMember.createdAt().toString(),
                aMember.updatedAt().toString()
        );
    }

    static CastMemberListResponse present(final CastMemberListOutput aMember) {
        return new CastMemberListResponse(
                aMember.id(),
                aMember.name(),
                aMember.type().name(),
                aMember.createdAt().toString()
        );
    }
}
