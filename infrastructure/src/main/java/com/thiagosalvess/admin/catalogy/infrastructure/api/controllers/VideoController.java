package com.thiagosalvess.admin.catalogy.infrastructure.api.controllers;

import com.thiagosalvess.admin.catalogy.application.video.create.CreateVideoCommand;
import com.thiagosalvess.admin.catalogy.application.video.create.CreateVideoUseCase;
import com.thiagosalvess.admin.catalogy.application.video.delete.DeleteVideoUseCase;
import com.thiagosalvess.admin.catalogy.application.video.media.get.GetMediaCommand;
import com.thiagosalvess.admin.catalogy.application.video.media.get.GetMediaUseCase;
import com.thiagosalvess.admin.catalogy.application.video.media.upload.UploadMediaCommand;
import com.thiagosalvess.admin.catalogy.application.video.media.upload.UploadMediaUseCase;
import com.thiagosalvess.admin.catalogy.application.video.retrieve.get.GetVideoByIdUseCase;
import com.thiagosalvess.admin.catalogy.application.video.retrieve.list.ListVideosUseCase;
import com.thiagosalvess.admin.catalogy.application.video.update.UpdateVideoCommand;
import com.thiagosalvess.admin.catalogy.application.video.update.UpdateVideoUseCase;
import com.thiagosalvess.admin.catalogy.domain.castmember.CastMemberID;
import com.thiagosalvess.admin.catalogy.domain.category.CategoryID;
import com.thiagosalvess.admin.catalogy.domain.exceptions.NotificationException;
import com.thiagosalvess.admin.catalogy.domain.genre.GenreID;
import com.thiagosalvess.admin.catalogy.domain.pagination.Pagination;
import com.thiagosalvess.admin.catalogy.domain.validation.Error;
import com.thiagosalvess.admin.catalogy.domain.resource.Resource;
import com.thiagosalvess.admin.catalogy.domain.video.VideoMediaType;
import com.thiagosalvess.admin.catalogy.domain.video.VideoResource;
import com.thiagosalvess.admin.catalogy.domain.video.VideoSearchQuery;
import com.thiagosalvess.admin.catalogy.infrastructure.api.VideoAPI;
import com.thiagosalvess.admin.catalogy.infrastructure.utils.HashingUtils;
import com.thiagosalvess.admin.catalogy.infrastructure.video.models.CreateVideoRequest;
import com.thiagosalvess.admin.catalogy.infrastructure.video.models.UpdateVideoRequest;
import com.thiagosalvess.admin.catalogy.infrastructure.video.models.VideoListResponse;
import com.thiagosalvess.admin.catalogy.infrastructure.video.models.VideoResponse;
import com.thiagosalvess.admin.catalogy.infrastructure.video.presenters.VideoApiPresenter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.Objects;
import java.util.Set;

import static com.thiagosalvess.admin.catalogy.domain.utils.CollectionUtils.mapTo;

@RestController
public class VideoController implements VideoAPI {

    private final CreateVideoUseCase createVideoUseCase;
    private final GetVideoByIdUseCase getVideoByIdUseCase;
    private final UpdateVideoUseCase updateVideoUseCase;
    private final DeleteVideoUseCase deleteVideoUseCase;
    private final ListVideosUseCase listVideosUseCase;
    private final GetMediaUseCase getMediaUseCase;
    private final UploadMediaUseCase uploadMediaUseCase;

    public VideoController(
            final CreateVideoUseCase createVideoUseCase,
            final GetVideoByIdUseCase getVideoByIdUseCase,
            final UpdateVideoUseCase updateVideoUseCase,
            final DeleteVideoUseCase deleteVideoUseCase,
            final ListVideosUseCase listVideosUseCase,
            final GetMediaUseCase getMediaUseCase,
            final UploadMediaUseCase uploadMediaUseCase
    ) {
        this.createVideoUseCase = Objects.requireNonNull(createVideoUseCase);
        this.getVideoByIdUseCase = Objects.requireNonNull(getVideoByIdUseCase);
        this.updateVideoUseCase = Objects.requireNonNull(updateVideoUseCase);
        this.deleteVideoUseCase = Objects.requireNonNull(deleteVideoUseCase);
        this.listVideosUseCase = Objects.requireNonNull(listVideosUseCase);
        this.getMediaUseCase = Objects.requireNonNull(getMediaUseCase);
        this.uploadMediaUseCase = Objects.requireNonNull(uploadMediaUseCase);
    }

    @Override
    public Pagination<VideoListResponse> list(
            final String search,
            final int page,
            final int perPage,
            final String sort,
            final String direction,
            final Set<String> castMembers,
            final Set<String> categories,
            final Set<String> genres
    ) {
        final var castMemberIDs = mapTo(castMembers, CastMemberID::from);
        final var categoriesIDs = mapTo(categories, CategoryID::from);
        final var genresIDs = mapTo(genres, GenreID::from);

        final var aQuery =
                new VideoSearchQuery(page, perPage, search, sort, direction, castMemberIDs, categoriesIDs, genresIDs);

        return VideoApiPresenter.present(this.listVideosUseCase.execute(aQuery));
    }

    @Override
    public ResponseEntity<?> createFull(
            final String aTitle,
            final String aDescription,
            final Integer launchedAt,
            final Double aDuration,
            final Boolean wasOpened,
            final Boolean wasPublished,
            final String aRating,
            final Set<String> categories,
            final Set<String> castMembers,
            final Set<String> genres,
            final MultipartFile videoFile,
            final MultipartFile trailerFile,
            final MultipartFile bannerFile,
            final MultipartFile thumbFile,
            final MultipartFile thumbHalfFile
    ) {
        final var aCmd = CreateVideoCommand.with(
                aTitle,
                aDescription,
                launchedAt,
                aDuration,
                wasOpened,
                wasPublished,
                aRating,
                categories,
                genres,
                castMembers,
                resourceOf(videoFile),
                resourceOf(trailerFile),
                resourceOf(bannerFile),
                resourceOf(thumbFile),
                resourceOf(thumbHalfFile)
        );

        final var output = this.createVideoUseCase.execute(aCmd);

        return ResponseEntity.created(URI.create("/videos/" + output.id())).body(output);
    }

    @Override
    public ResponseEntity<?> createPartial(final CreateVideoRequest payload) {
        final var aCmd = CreateVideoCommand.with(
                payload.title(),
                payload.description(),
                payload.yearLaunched(),
                payload.duration(),
                payload.opened(),
                payload.published(),
                payload.rating(),
                payload.categories(),
                payload.genres(),
                payload.castMembers()
        );

        final var output = this.createVideoUseCase.execute(aCmd);

        return ResponseEntity.created(URI.create("/videos/" + output.id())).body(output);
    }

    @Override
    public VideoResponse getById(final String anId) {
        return VideoApiPresenter.present(this.getVideoByIdUseCase.execute(anId));
    }

    @Override
    public ResponseEntity<?> update(final String id, final UpdateVideoRequest payload) {
        final var aCmd = UpdateVideoCommand.with(
                id,
                payload.title(),
                payload.description(),
                payload.yearLaunched(),
                payload.duration(),
                payload.opened(),
                payload.published(),
                payload.rating(),
                payload.categories(),
                payload.genres(),
                payload.castMembers()
        );

        final var output = this.updateVideoUseCase.execute(aCmd);

        return ResponseEntity.ok()
                .location(URI.create("/videos/" + output.id()))
                .body(VideoApiPresenter.present(output));
    }

    @Override
    public void deleteById(final String id) {
        this.deleteVideoUseCase.execute(id);
    }

    @Override
    public ResponseEntity<byte[]> getMediaByType(final String id, final String type) {
        final var aMedia =
                this.getMediaUseCase.execute(GetMediaCommand.with(id, type));

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(aMedia.contentType()))
                .contentLength(aMedia.content().length)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=%s".formatted(aMedia.name()))
                .body(aMedia.content());
    }

    @Override
    public ResponseEntity<?> uploadMediaByType(final String id, final String type, final MultipartFile media) {
        final var aType = VideoMediaType.of(type)
                .orElseThrow(() -> NotificationException.with(new Error("Invalid %s for VideoMediaType".formatted(type))));

        final var aCmd =
                UploadMediaCommand.with(id, VideoResource.with(resourceOf(media), aType));

        final var output = this.uploadMediaUseCase.execute(aCmd);

        return ResponseEntity
                .created(URI.create("/videos/%s/medias/%s".formatted(id, type)))
                .body(VideoApiPresenter.present(output));
    }

    private Resource resourceOf(final MultipartFile part) {
        if (part == null) {
            return null;
        }

        try {
            return Resource.with(
                    part.getBytes(),
                    HashingUtils.checksum(part.getBytes()),
                    part.getContentType(),
                    part.getOriginalFilename()
            );
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}
