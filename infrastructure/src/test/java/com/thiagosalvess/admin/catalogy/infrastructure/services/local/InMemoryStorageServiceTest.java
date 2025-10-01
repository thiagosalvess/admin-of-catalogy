package com.thiagosalvess.admin.catalogy.infrastructure.services.local;

import com.thiagosalvess.admin.catalogy.domain.Fixture;
import com.thiagosalvess.admin.catalogy.domain.video.VideoMediaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryStorageServiceTest {
    private InMemoryStorageService target = new InMemoryStorageService();

    @BeforeEach
    public void setUp() {
        this.target.reset();
    }

    @Test
    public void givenValidResource_whenCallsStore_shouldStoreIt() {
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final var expectedId = "item";

        target.store(expectedId, expectedResource);

        final var actualContent = this.target.storage().get(expectedId);

        assertEquals(expectedResource, actualContent);
    }

    @Test
    public void givenResource_whenCallsGet_shouldRetrieveIt() {
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final var expectedId = "item";

        this.target.storage().put(expectedId, expectedResource);

        final var actualContent = target.get(expectedId).get();

        assertEquals(expectedResource, actualContent);
    }

    @Test
    public void givenInvalidResource_whenCallsGet_shouldRetrieveEmpty() {
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final var expectedId = "jajaja";

        this.target.storage().put("item", expectedResource);

        final var actualContent = target.get(expectedId);

        assertTrue(actualContent.isEmpty());
    }

    @Test
    public void givenPrefix_whenCallsList_shouldRetrieveAll() {
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);

        final var expectedIds = List.of("item1", "item2");

        this.target.storage().put("item1", expectedResource);
        this.target.storage().put("item2", expectedResource);

        final var actualContent = target.list("it");

        assertTrue(
                expectedIds.size() == actualContent.size()
                        && expectedIds.containsAll(actualContent)
        );
    }

    @Test
    public void givenResource_whenCallsDeleteAll_shouldEmptyStorage() {
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);

        final var expectedIds = List.of("item1", "item2");

        this.target.storage().put("item1", expectedResource);
        this.target.storage().put("item2", expectedResource);

        target.deleteAll(expectedIds);

        assertTrue(this.target.storage().isEmpty());
    }
}