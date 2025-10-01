package com.thiagosalvess.admin.catalogy.application.castmember.create;

import com.thiagosalvess.admin.catalogy.application.UseCase;

public sealed abstract class CreateCastMemberUseCase
        extends UseCase<CreateCastMemberCommand, CreateCastMemberOutput>
        permits DefaultCreateCastMemberUseCase {
}
