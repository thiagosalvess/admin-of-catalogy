package com.thiagosalvess.admin.catalogy.domain.video;

import com.thiagosalvess.admin.catalogy.domain.castmember.CastMemberID;
import com.thiagosalvess.admin.catalogy.domain.category.CategoryID;
import com.thiagosalvess.admin.catalogy.domain.genre.GenreID;

import java.util.Set;

public record VideoSearchQuery(
        int page,
        int perPage,
        String terms,
        String sort,
        String direction,
        Set<CastMemberID> castMembers,
        Set<CategoryID> categories,
        Set<GenreID> genres
) {
}
