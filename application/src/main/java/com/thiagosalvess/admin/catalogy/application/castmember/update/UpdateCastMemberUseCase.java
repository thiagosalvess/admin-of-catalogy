package com.thiagosalvess.admin.catalogy.application.castmember.update;

import com.thiagosalvess.admin.catalogy.application.UseCase;

public sealed  abstract class UpdateCastMemberUseCase
        extends UseCase<UpdateCastMemberCommand, UpdateCastMemberOutput>
        permits DefaultUpdateCastMemberUseCase {
}
