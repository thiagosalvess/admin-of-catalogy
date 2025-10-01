package com.thiagosalvess.admin.catalogy.infrastructure.configuration.usecases;

import com.thiagosalvess.admin.catalogy.application.category.create.CreateCategoryUseCase;
import com.thiagosalvess.admin.catalogy.application.category.create.DefaultCreateCategoryUseCase;
import com.thiagosalvess.admin.catalogy.application.category.delete.DefaultDeleteCategoryUseCase;
import com.thiagosalvess.admin.catalogy.application.category.delete.DeleteCategoryUseCase;
import com.thiagosalvess.admin.catalogy.application.category.retrieve.get.DefaultGetCategoryByIdUseCase;
import com.thiagosalvess.admin.catalogy.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.thiagosalvess.admin.catalogy.application.category.retrieve.list.DefaultListCategoriesUseCase;
import com.thiagosalvess.admin.catalogy.application.category.retrieve.list.ListCategoriesUseCase;
import com.thiagosalvess.admin.catalogy.application.category.update.DefaultUpdateCategoryUseCase;
import com.thiagosalvess.admin.catalogy.application.category.update.UpdateCategoryUseCase;
import com.thiagosalvess.admin.catalogy.domain.category.CategoryGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class CategoryUseCaseConfig {
    private final CategoryGateway categoryGateway;

    public CategoryUseCaseConfig(CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Bean
    public CreateCategoryUseCase createCategoryUseCase() {
        return new DefaultCreateCategoryUseCase(categoryGateway);
    }

    @Bean
    public UpdateCategoryUseCase updateCategoryUseCase() {
        return new DefaultUpdateCategoryUseCase(categoryGateway);
    }

    @Bean
    public GetCategoryByIdUseCase getCategoryByIdUseCase() {
        return new DefaultGetCategoryByIdUseCase(categoryGateway);
    }

    @Bean
    public ListCategoriesUseCase listCategoryByIdUseCase() {
        return new DefaultListCategoriesUseCase(categoryGateway);
    }

    @Bean
    public DeleteCategoryUseCase deleteCategoryByIdUseCase() {
        return new DefaultDeleteCategoryUseCase(categoryGateway);
    }
}
