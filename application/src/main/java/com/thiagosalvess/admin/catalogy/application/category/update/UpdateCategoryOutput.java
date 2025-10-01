package com.thiagosalvess.admin.catalogy.application.category.update;

import com.thiagosalvess.admin.catalogy.domain.category.Category;
import com.thiagosalvess.admin.catalogy.domain.category.CategoryID;

public record UpdateCategoryOutput(
        String id
) {
    public static UpdateCategoryOutput from(final String anId) {
        return new UpdateCategoryOutput(anId);
    }

    public static UpdateCategoryOutput from(final Category aCategory) {
        return new UpdateCategoryOutput(aCategory.getId().getValue());
    }
}
