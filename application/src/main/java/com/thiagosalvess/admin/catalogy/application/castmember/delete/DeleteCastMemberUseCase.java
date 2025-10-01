package com.thiagosalvess.admin.catalogy.application.castmember.delete;

import com.thiagosalvess.admin.catalogy.application.UnitUseCase;

public sealed abstract class DeleteCastMemberUseCase
        extends UnitUseCase<String>
        permits DefaultDeleteCastMemberUseCase {
}
