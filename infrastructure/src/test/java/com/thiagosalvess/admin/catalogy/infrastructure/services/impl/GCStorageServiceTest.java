package com.thiagosalvess.admin.catalogy.infrastructure.services.impl;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.thiagosalvess.admin.catalogy.domain.Fixture;
import com.thiagosalvess.admin.catalogy.domain.resource.Resource;
import com.thiagosalvess.admin.catalogy.domain.video.VideoMediaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.wildfly.common.Assert.assertTrue;

class GCStorageServiceTest {
    private GCStorageService target;

    private Storage storage;

    private String bucket = "test";

    @BeforeEach
    public void setUp() {
        this.storage = mock(Storage.class);
        this.target = new GCStorageService(bucket, storage);
    }

    @Test
    public void givenValidResource_whenCallsStore_shouldStoreIt() {
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final var expectedId = expectedResource.name();

        final Blob blob = mockBlob(expectedResource);
        doReturn(blob).when(storage).get(eq(bucket), eq(expectedId));

        this.target.store(expectedId, expectedResource);

        final var capturer = ArgumentCaptor.forClass(BlobInfo.class);

        verify(storage, times(1)).create(capturer.capture(), eq(expectedResource.content()));

        final var actualBlob = capturer.getValue();
        assertEquals(this.bucket, actualBlob.getBlobId().getBucket());
        assertEquals(expectedId, actualBlob.getBlobId().getName());
        assertEquals(expectedResource.contentType(), actualBlob.getContentType());
        assertEquals(expectedResource.checksum(), actualBlob.getCrc32cToHexString());
    }

    @Test
    public void givenResource_whenCallsGet_shouldRetrieveIt() {
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final var expectedId = expectedResource.name();

        final Blob blob = mockBlob(expectedResource);
        doReturn(blob).when(storage).get(eq(bucket), eq(expectedId));

        final var actualContent = target.get(expectedId).get();

        assertEquals(expectedResource.checksum(), actualContent.checksum());
        assertEquals(expectedResource.name(), actualContent.name());
        assertEquals(expectedResource.content(), actualContent.content());
        assertEquals(expectedResource.contentType(), actualContent.contentType());
    }

    @Test
    public void givenInvalidResource_whenCallsGet_shouldRetrieveEmpty() {
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final var expectedId = expectedResource.name();

        doReturn(null).when(storage).get(eq(bucket), eq(expectedId));

        final var actualContent = target.get(expectedId);

        assertTrue(actualContent.isEmpty());
    }

    @Test
    public void givenPrefix_whenCallsList_shouldRetrieveAll() {
        final var video = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final var banner = Fixture.Videos.resource(VideoMediaType.BANNER);
        final var expectedIds = List.of(video.name(), banner.name());

        final var page = mock(Page.class);

        final Blob blob1 = mockBlob(video);
        final Blob blob2 = mockBlob(banner);

        doReturn(List.of(blob1, blob2)).when(page).iterateAll();
        doReturn(page).when(storage).list(eq(bucket), eq(Storage.BlobListOption.prefix("it")));

        final var actualContent = target.list("it");

        assertTrue(
                expectedIds.size() == actualContent.size()
                        && expectedIds.containsAll(actualContent)
        );
    }

    @Test
    public void givenResource_whenCallsDeleteAll_shouldEmptyStorage() {
        final var expectedIds = List.of("item1", "item2");

        target.deleteAll(expectedIds);

        final var capturer = ArgumentCaptor.forClass(List.class);

        verify(storage, times(1)).delete(capturer.capture());

        final var actualIds = ((List<BlobId>) capturer.getValue()).stream()
                .map(BlobId::getName)
                .toList();

        assertTrue(expectedIds.size() == actualIds.size() && actualIds.containsAll(expectedIds));
    }

    private Blob mockBlob(final Resource resource) {
        final var blob1 = mock(Blob.class);
        when(blob1.getBlobId()).thenReturn(BlobId.of(bucket, resource.name()));
        when(blob1.getCrc32cToHexString()).thenReturn(resource.checksum());
        when(blob1.getContent()).thenReturn(resource.content());
        when(blob1.getContentType()).thenReturn(resource.contentType());
        when(blob1.getName()).thenReturn(resource.name());
        return blob1;
    }
}