package com.thiagosalvess.admin.catalogy.application.castmember.retrieve.list;

import com.thiagosalvess.admin.catalogy.application.UseCase;
import com.thiagosalvess.admin.catalogy.domain.pagination.Pagination;
import com.thiagosalvess.admin.catalogy.domain.pagination.SearchQuery;

public sealed abstract class ListCastMembersUseCase
        extends UseCase<SearchQuery, Pagination<CastMemberListOutput>>
        permits DefaultListCastMembersUseCase{
}
