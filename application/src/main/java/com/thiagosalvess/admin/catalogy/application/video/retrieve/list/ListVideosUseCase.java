package com.thiagosalvess.admin.catalogy.application.video.retrieve.list;

import com.thiagosalvess.admin.catalogy.application.UseCase;
import com.thiagosalvess.admin.catalogy.domain.pagination.Pagination;
import com.thiagosalvess.admin.catalogy.domain.video.VideoSearchQuery;

public abstract class ListVideosUseCase extends UseCase<VideoSearchQuery, Pagination<VideoListOutput>> {
}
