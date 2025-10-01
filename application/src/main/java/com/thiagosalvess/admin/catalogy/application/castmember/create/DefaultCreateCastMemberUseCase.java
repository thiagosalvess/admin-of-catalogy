package com.thiagosalvess.admin.catalogy.application.castmember.create;

import com.thiagosalvess.admin.catalogy.domain.castmember.CastMember;
import com.thiagosalvess.admin.catalogy.domain.castmember.CastMemberGateway;
import com.thiagosalvess.admin.catalogy.domain.exceptions.NotificationException;
import com.thiagosalvess.admin.catalogy.domain.validation.handler.Notification;

import java.util.Objects;

public non-sealed class DefaultCreateCastMemberUseCase extends CreateCastMemberUseCase {

    private final CastMemberGateway castMemberGateway;

    public DefaultCreateCastMemberUseCase(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public CreateCastMemberOutput execute(final CreateCastMemberCommand aCommand) {
        final var aName = aCommand.name();
        final var aType = aCommand.type();

        final var notification = Notification.create();
        final var aMember = notification.validate(() -> CastMember.newMember(aName, aType));
        if (notification.hasError()) {
            notify(notification);
        }
        return CreateCastMemberOutput.from(this.castMemberGateway.create(aMember));
    }

    private static void notify(Notification notification) {
        throw new NotificationException("Could not create Aggregate CastMember", notification);
    }
}
