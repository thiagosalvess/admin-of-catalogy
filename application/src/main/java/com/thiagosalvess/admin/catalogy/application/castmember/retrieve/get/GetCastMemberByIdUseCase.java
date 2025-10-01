package com.thiagosalvess.admin.catalogy.application.castmember.retrieve.get;

import com.thiagosalvess.admin.catalogy.application.UseCase;

public sealed abstract class GetCastMemberByIdUseCase
        extends UseCase<String, CastMemberOutput>
        permits DefaultGetCastMemberByIdUseCase {
}
