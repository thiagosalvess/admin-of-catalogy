package com.thiagosalvess.admin.catalogy.infrastructure.genre.presenters;

import com.thiagosalvess.admin.catalogy.application.genre.retrieve.get.GenreOutput;
import com.thiagosalvess.admin.catalogy.application.genre.retrieve.list.GenreListOutput;
import com.thiagosalvess.admin.catalogy.infrastructure.genre.models.GenreListResponse;
import com.thiagosalvess.admin.catalogy.infrastructure.genre.models.GenreResponse;

public interface GenreAPIPresenter {
    static GenreResponse present(final GenreOutput output) {
        return new GenreResponse(
                output.id(),
                output.name(),
                output.categories(),
                output.isActive(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt()
        );
    }

    static GenreListResponse present(final GenreListOutput output) {
        return new GenreListResponse(
                output.id(),
                output.name(),
                output.isActive(),
                output.createdAt(),
                output.deletedAt()
        );
    }
}
