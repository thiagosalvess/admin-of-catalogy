package com.thiagosalvess.admin.catalogy.application.category.retrieve.list;

import com.thiagosalvess.admin.catalogy.application.UseCase;
import com.thiagosalvess.admin.catalogy.domain.pagination.SearchQuery;
import com.thiagosalvess.admin.catalogy.domain.pagination.Pagination;

public abstract class ListCategoriesUseCase extends UseCase<SearchQuery, Pagination<CategoryListOutput>> {
}
