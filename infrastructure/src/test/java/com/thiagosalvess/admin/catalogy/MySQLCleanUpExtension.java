package com.thiagosalvess.admin.catalogy;

import com.thiagosalvess.admin.catalogy.infrastructure.castmember.persistence.CastMemberRepository;
import com.thiagosalvess.admin.catalogy.infrastructure.category.persistence.CategoryRepository;
import com.thiagosalvess.admin.catalogy.infrastructure.genre.persistence.GenreRepository;
import com.thiagosalvess.admin.catalogy.infrastructure.video.persistence.VideoRepository;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;
import java.util.List;

public class MySQLCleanUpExtension implements BeforeEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) {
        final var appContext = SpringExtension.getApplicationContext(context);

        cleanUp(List.of(
                appContext.getBean(VideoRepository.class),
                appContext.getBean(CastMemberRepository.class),
                appContext.getBean(GenreRepository.class),
                appContext.getBean(CategoryRepository.class)
        ));
    }

    private void cleanUp(final Collection<CrudRepository> repositories) {
        repositories.forEach(CrudRepository::deleteAll);
    }
}
