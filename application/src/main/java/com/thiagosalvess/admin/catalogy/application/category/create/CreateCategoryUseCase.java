package com.thiagosalvess.admin.catalogy.application.category.create;

import com.thiagosalvess.admin.catalogy.application.UseCase;
import com.thiagosalvess.admin.catalogy.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class CreateCategoryUseCase extends UseCase<CreateCategoryCommand, Either<Notification, CreateCategoryOutput>> {
}
