package com.thiagosalvess.admin.catalogy.application.genre.delete;

import com.thiagosalvess.admin.catalogy.domain.genre.GenreGateway;
import com.thiagosalvess.admin.catalogy.domain.genre.GenreID;

import java.util.Objects;

public class DefaultDeleteGenreUseCase extends DeleteGenreUseCase {

    private final GenreGateway genreGateway;

    public DefaultDeleteGenreUseCase(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public void execute(String anId) {
        this.genreGateway.deleteById(GenreID.from(anId));
    }
}
