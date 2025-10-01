package com.thiagosalvess.admin.catalogy.infrastructure.category.presenters;

import com.thiagosalvess.admin.catalogy.application.category.retrieve.get.CategoryOutput;
import com.thiagosalvess.admin.catalogy.application.category.retrieve.list.CategoryListOutput;
import com.thiagosalvess.admin.catalogy.infrastructure.category.models.CategoryResponse;
import com.thiagosalvess.admin.catalogy.infrastructure.category.models.CategoryListResponse;

public interface CategoryAPIPresenter {

    static CategoryResponse present(final CategoryOutput output){
        return new CategoryResponse(
                output.id().getValue(),
                output.name(),
                output.description(),
                output.isActive(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt()
        );
    }

    static CategoryListResponse present(final CategoryListOutput output){
        return new CategoryListResponse(
                output.id().getValue(),
                output.name(),
                output.description(),
                output.isActive(),
                output.createdAt(),
                output.deletedAt()
        );
    }
}
