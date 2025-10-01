package com.thiagosalvess.admin.catalogy.application.genre.retrieve.get;

import com.thiagosalvess.admin.catalogy.domain.exceptions.NotFoundException;
import com.thiagosalvess.admin.catalogy.domain.genre.Genre;
import com.thiagosalvess.admin.catalogy.domain.genre.GenreGateway;
import com.thiagosalvess.admin.catalogy.domain.genre.GenreID;

import java.util.Objects;

public class DefaultGetGenreByIdUseCase extends GetGenreByIdUseCase {

    private final GenreGateway genreGateway;

    public DefaultGetGenreByIdUseCase(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public GenreOutput execute(String anId) {
        final var aGenreId = GenreID.from(anId);
        return this.genreGateway.findById(aGenreId).map(GenreOutput::from)
                .orElseThrow(() -> NotFoundException.with(Genre.class, aGenreId));
    }
}
