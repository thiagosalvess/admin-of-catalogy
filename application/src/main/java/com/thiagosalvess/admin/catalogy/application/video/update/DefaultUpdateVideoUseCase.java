package com.thiagosalvess.admin.catalogy.application.video.update;

import com.thiagosalvess.admin.catalogy.domain.Identifier;
import com.thiagosalvess.admin.catalogy.domain.castmember.CastMemberGateway;
import com.thiagosalvess.admin.catalogy.domain.castmember.CastMemberID;
import com.thiagosalvess.admin.catalogy.domain.category.CategoryGateway;
import com.thiagosalvess.admin.catalogy.domain.category.CategoryID;
import com.thiagosalvess.admin.catalogy.domain.exceptions.DomainException;
import com.thiagosalvess.admin.catalogy.domain.exceptions.InternalErrorException;
import com.thiagosalvess.admin.catalogy.domain.exceptions.NotFoundException;
import com.thiagosalvess.admin.catalogy.domain.exceptions.NotificationException;
import com.thiagosalvess.admin.catalogy.domain.genre.GenreGateway;
import com.thiagosalvess.admin.catalogy.domain.genre.GenreID;
import com.thiagosalvess.admin.catalogy.domain.validation.Error;
import com.thiagosalvess.admin.catalogy.domain.validation.ValidationHandler;
import com.thiagosalvess.admin.catalogy.domain.validation.handler.Notification;
import com.thiagosalvess.admin.catalogy.domain.video.*;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.thiagosalvess.admin.catalogy.domain.video.VideoMediaType.*;

public class DefaultUpdateVideoUseCase extends UpdateVideoUseCase {
    private final VideoGateway videoGateway;
    private final CategoryGateway categoryGateway;
    private final CastMemberGateway castMemberGateway;
    private final GenreGateway genreGateway;
    private final MediaResourceGateway mediaResourceGateway;

    public DefaultUpdateVideoUseCase(
            final VideoGateway videoGateway,
            final CategoryGateway categoryGateway,
            final CastMemberGateway castMemberGateway,
            final GenreGateway genreGateway,
            final MediaResourceGateway mediaResourceGateway
    ) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
        this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
    }

    @Override
    public UpdateVideoOutput execute(final UpdateVideoCommand aCommand) {
        final var anId = VideoID.from(aCommand.id());
        final var aRating = Rating.of(aCommand.rating()).orElse(null);
        final var aLaunchYear = aCommand.launchedAt() != null ? Year.of(aCommand.launchedAt()) : null;
        final var categories = toIdentifier(aCommand.categories(), CategoryID::from);
        final var genres = toIdentifier(aCommand.genres(), GenreID::from);
        final var members = toIdentifier(aCommand.members(), CastMemberID::from);

        final var aVideo = this.videoGateway.findById(anId)
                .orElseThrow(notFoundException(anId));

        final var notification = Notification.create();
        notification.append(validateCategories(categories));
        notification.append(validateGenres(genres));
        notification.append(validateMembers(members));

        aVideo.update(
                aCommand.title(),
                aCommand.description(),
                aLaunchYear,
                aCommand.duration(),
                aCommand.opened(),
                aCommand.published(),
                aRating,
                categories,
                genres,
                members
        );

        aVideo.validate(notification);

        if (notification.hasError()) {
            throw new NotificationException("Could not update Aggregate Video", notification);
        }

        return UpdateVideoOutput.from(update(aCommand, aVideo));
    }

    private Video update(final UpdateVideoCommand aCommand, final Video aVideo) {
        final var anId = aVideo.getId();

        try {
            final var aVideoMedia = aCommand.getVideo()
                    .map(it -> this.mediaResourceGateway.storeAudioVideo(anId, VideoResource.with(it, VIDEO)))
                    .orElse(null);

            final var aTrailerMedia = aCommand.getTrailer()
                    .map(it -> this.mediaResourceGateway.storeAudioVideo(anId, VideoResource.with(it, TRAILER)))
                    .orElse(null);

            final var aBannerMedia = aCommand.getBanner()
                    .map(it -> this.mediaResourceGateway.storeImage(anId, VideoResource.with(it, BANNER)))
                    .orElse(null);

            final var aThumbnailMedia = aCommand.getThumbnail()
                    .map(it -> this.mediaResourceGateway.storeImage(anId, VideoResource.with(it, THUMBNAIL)))
                    .orElse(null);

            final var aThumbHalfMedia = aCommand.getThumbnailHalf()
                    .map(it -> this.mediaResourceGateway.storeImage(anId, VideoResource.with(it, THUMBNAIL_HALF)))
                    .orElse(null);

            return this.videoGateway.update(
                    aVideo
                            .updateVideoMedia(aVideoMedia)
                            .updateTrailerMedia(aTrailerMedia)
                            .updateBannerMedia(aBannerMedia)
                            .updateThumbnailMedia(aThumbnailMedia)
                            .updateThumbnailHalfMedia(aThumbHalfMedia)
            );
        } catch (final Throwable t) {
            throw InternalErrorException.with(
                    "An error on create video was observed [videoId:%s]".formatted(anId.getValue()),
                    t
            );
        }
    }

    private Supplier<DomainException> notFoundException(final VideoID anId) {
        return () -> NotFoundException.with(Video.class, anId);
    }

    private ValidationHandler validateCategories(final Set<CategoryID> ids) {
        return validateAggregate("categories", ids, categoryGateway::existsByIds);
    }

    private ValidationHandler validateGenres(final Set<GenreID> ids) {
        return validateAggregate("genres", ids, genreGateway::existsByIds);
    }

    private ValidationHandler validateMembers(final Set<CastMemberID> ids) {
        return validateAggregate("cast members", ids, castMemberGateway::existsByIds);
    }

    private <T extends Identifier> ValidationHandler validateAggregate(
            final String aggregate,
            final Set<T> ids,
            final Function<Iterable<T>, List<T>> existsByIds
    ) {
        final var notification = Notification.create();
        if (ids == null || ids.isEmpty()) {
            return notification;
        }

        final var retrievedIds = existsByIds.apply(ids);

        if (ids.size() != retrievedIds.size()) {
            final var missingIds = new ArrayList<>(ids);
            missingIds.removeAll(retrievedIds);

            final var missingIdsMessage = missingIds.stream()
                    .map(Identifier::getValue)
                    .collect(Collectors.joining(", "));

            notification.append(new Error("Some %s could not be found: %s".formatted(aggregate, missingIdsMessage)));
        }

        return notification;
    }

    private <T> Set<T> toIdentifier(final Set<String> ids, final Function<String, T> mapper) {
        return ids.stream()
                .map(mapper)
                .collect(Collectors.toSet());
    }
}
