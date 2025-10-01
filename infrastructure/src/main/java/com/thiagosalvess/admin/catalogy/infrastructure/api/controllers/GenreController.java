package com.thiagosalvess.admin.catalogy.infrastructure.api.controllers;

import com.thiagosalvess.admin.catalogy.application.genre.create.CreateGenreCommand;
import com.thiagosalvess.admin.catalogy.application.genre.create.CreateGenreUseCase;
import com.thiagosalvess.admin.catalogy.application.genre.delete.DeleteGenreUseCase;
import com.thiagosalvess.admin.catalogy.application.genre.retrieve.get.GetGenreByIdUseCase;
import com.thiagosalvess.admin.catalogy.application.genre.retrieve.list.ListGenreUseCase;
import com.thiagosalvess.admin.catalogy.application.genre.update.UpdateGenreCommand;
import com.thiagosalvess.admin.catalogy.application.genre.update.UpdateGenreUseCase;
import com.thiagosalvess.admin.catalogy.domain.pagination.Pagination;
import com.thiagosalvess.admin.catalogy.domain.pagination.SearchQuery;
import com.thiagosalvess.admin.catalogy.infrastructure.api.GenreAPI;
import com.thiagosalvess.admin.catalogy.infrastructure.genre.models.CreateGenreRequest;
import com.thiagosalvess.admin.catalogy.infrastructure.genre.models.GenreListResponse;
import com.thiagosalvess.admin.catalogy.infrastructure.genre.models.GenreResponse;
import com.thiagosalvess.admin.catalogy.infrastructure.genre.models.UpdateGenreRequest;
import com.thiagosalvess.admin.catalogy.infrastructure.genre.presenters.GenreAPIPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class GenreController implements GenreAPI {

    private final CreateGenreUseCase createGenreUseCase;
    private final DeleteGenreUseCase deleteGenreUseCase;
    private final GetGenreByIdUseCase getGenreByIdUseCase;
    private final ListGenreUseCase listGenreUseCase;
    private final UpdateGenreUseCase updateGenreUseCase;

    public GenreController(
            final CreateGenreUseCase createGenreUseCase,
            final DeleteGenreUseCase deleteGenreUseCase,
            final GetGenreByIdUseCase getGenreByIdUseCase,
            final ListGenreUseCase listGenreUseCase,
            final UpdateGenreUseCase updateGenreUseCase
    ) {
        this.createGenreUseCase = Objects.requireNonNull(createGenreUseCase);
        this.deleteGenreUseCase = Objects.requireNonNull(deleteGenreUseCase);
        this.getGenreByIdUseCase = Objects.requireNonNull(getGenreByIdUseCase);
        this.listGenreUseCase = Objects.requireNonNull(listGenreUseCase);
        this.updateGenreUseCase = Objects.requireNonNull(updateGenreUseCase);
    }

    @Override
    public ResponseEntity<?> create(final CreateGenreRequest input) {
        final var aCommand =
                CreateGenreCommand.with(
                        input.name(),
                        input.isActive(),
                        input.categories()
                );

        final var output = this.createGenreUseCase.execute(aCommand);

        return ResponseEntity.created(URI.create("/genres/" + output.id())).body(output);
    }

    @Override
    public Pagination<GenreListResponse> list(
            final String search,
            final int page,
            final int perPage,
            final String sort,
            final String direction
    ) {
        return this.listGenreUseCase.execute(new SearchQuery(page, perPage, search, sort, direction))
                .map(GenreAPIPresenter::present);
    }

    @Override
    public GenreResponse getById(final String id) {
        return GenreAPIPresenter.present(this.getGenreByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> updateById(final String id, final UpdateGenreRequest input) {
        final var aCommand =
                UpdateGenreCommand.with(
                        id,
                        input.name(),
                        input.isActive(),
                        input.categories()
                );

        final var output = this.updateGenreUseCase.execute(aCommand);

        return ResponseEntity.ok(output);
    }

    @Override
    public void deleteById(final String id) {
        this.deleteGenreUseCase.execute(id);
    }
}
