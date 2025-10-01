package com.thiagosalvess.admin.catalogy.application.genre.retrieve.list;

import com.thiagosalvess.admin.catalogy.application.UseCase;
import com.thiagosalvess.admin.catalogy.domain.pagination.Pagination;
import com.thiagosalvess.admin.catalogy.domain.pagination.SearchQuery;

public abstract class ListGenreUseCase extends UseCase<SearchQuery, Pagination<GenreListOutput>> {
}
