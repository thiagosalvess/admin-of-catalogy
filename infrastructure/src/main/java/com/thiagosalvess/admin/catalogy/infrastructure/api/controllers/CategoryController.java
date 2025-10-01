package com.thiagosalvess.admin.catalogy.infrastructure.api.controllers;

import com.thiagosalvess.admin.catalogy.application.category.create.CreateCategoryCommand;
import com.thiagosalvess.admin.catalogy.application.category.create.CreateCategoryOutput;
import com.thiagosalvess.admin.catalogy.application.category.create.CreateCategoryUseCase;
import com.thiagosalvess.admin.catalogy.application.category.delete.DeleteCategoryUseCase;
import com.thiagosalvess.admin.catalogy.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.thiagosalvess.admin.catalogy.application.category.retrieve.list.ListCategoriesUseCase;
import com.thiagosalvess.admin.catalogy.application.category.update.UpdateCategoryCommand;
import com.thiagosalvess.admin.catalogy.application.category.update.UpdateCategoryOutput;
import com.thiagosalvess.admin.catalogy.application.category.update.UpdateCategoryUseCase;
import com.thiagosalvess.admin.catalogy.domain.pagination.SearchQuery;
import com.thiagosalvess.admin.catalogy.domain.pagination.Pagination;
import com.thiagosalvess.admin.catalogy.domain.validation.handler.Notification;
import com.thiagosalvess.admin.catalogy.infrastructure.api.CategoryAPI;
import com.thiagosalvess.admin.catalogy.infrastructure.category.models.CategoryListResponse;
import com.thiagosalvess.admin.catalogy.infrastructure.category.models.CategoryResponse;
import com.thiagosalvess.admin.catalogy.infrastructure.category.models.CreateCategoryRequest;
import com.thiagosalvess.admin.catalogy.infrastructure.category.models.UpdateCategoryRequest;
import com.thiagosalvess.admin.catalogy.infrastructure.category.presenters.CategoryAPIPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;
import java.util.function.Function;

@RestController
public class CategoryController implements CategoryAPI {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final GetCategoryByIdUseCase getCategoryByIdUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;
    private final ListCategoriesUseCase listCategoriesUseCase;

    public CategoryController(
            final CreateCategoryUseCase createCategoryUseCase,
            final GetCategoryByIdUseCase getCategoryByIdUseCase, UpdateCategoryUseCase updateCategoryUseCase, DeleteCategoryUseCase deleteCategoryUseCase, ListCategoriesUseCase listCategoriesUseCase) {
        this.createCategoryUseCase = Objects.requireNonNull(createCategoryUseCase);
        this.getCategoryByIdUseCase = Objects.requireNonNull(getCategoryByIdUseCase);
        this.updateCategoryUseCase = Objects.requireNonNull(updateCategoryUseCase);
        this.deleteCategoryUseCase = Objects.requireNonNull(deleteCategoryUseCase);
        this.listCategoriesUseCase = Objects.requireNonNull(listCategoriesUseCase);
    }

    @Override
    public ResponseEntity<?> createCategory(final CreateCategoryRequest input) {
        final var aCommand = CreateCategoryCommand.with(
                input.name(),
                input.description(),
                input.active() != null ? input.active() : true
        );
        final Function<Notification, ResponseEntity<?>> onError =
                ResponseEntity.unprocessableEntity()::body;

        final Function<CreateCategoryOutput, ResponseEntity<?>> onSuccess = ouput ->
                ResponseEntity.created(URI.create("/categories/" + ouput.id())).body(ouput);

        return this.createCategoryUseCase.execute(aCommand)
                .fold(onError, onSuccess);
    }

    @Override
    public Pagination<CategoryListResponse> listCategories(
            final String search,
            final int page,
            final int perPage,
            final String sort,
            final String direction
    ) {
        return listCategoriesUseCase.execute(new SearchQuery(page, perPage, search, sort, direction))
                .map(CategoryAPIPresenter::present);
    }

    @Override
    public CategoryResponse getById(final String id) {
        return CategoryAPIPresenter.present(this.getCategoryByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> updateById(final String id, final UpdateCategoryRequest input) {
        final var aCommand = UpdateCategoryCommand.with(
                id,
                input.name(),
                input.description(),
                input.active() != null ? input.active() : true
        );
        final Function<Notification, ResponseEntity<?>> onError =
                ResponseEntity.unprocessableEntity()::body;

        final Function<UpdateCategoryOutput, ResponseEntity<?>> onSuccess =
                ResponseEntity::ok;

        return this.updateCategoryUseCase.execute(aCommand)
                .fold(onError, onSuccess);
    }

    @Override
    public void deleteById(String id) {
        this.deleteCategoryUseCase.execute(id);
    }
}
