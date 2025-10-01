package com.thiagosalvess.admin.catalogy.application.genre.update;

import com.thiagosalvess.admin.catalogy.domain.genre.Genre;

public record UpdateGenreOutput(
        String id
) {

    public static UpdateGenreOutput from(final Genre aGenre) {
        return new UpdateGenreOutput(aGenre.getId().getValue());
    }
}
