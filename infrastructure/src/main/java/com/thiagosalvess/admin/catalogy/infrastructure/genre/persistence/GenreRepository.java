package com.thiagosalvess.admin.catalogy.infrastructure.genre.persistence;

import com.thiagosalvess.admin.catalogy.infrastructure.category.persistence.CategoryJpaEntity;
import org.apache.commons.lang3.function.Failable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GenreRepository extends JpaRepository<GenreJpaEntity, String> {
    Page<GenreJpaEntity> findAll(Specification<GenreJpaEntity> whereClause, Pageable page);

    @Query(value = "select g.id from Genre g where g.id in :ids")
    List<String> existsByIds(List<String> ids);
}
