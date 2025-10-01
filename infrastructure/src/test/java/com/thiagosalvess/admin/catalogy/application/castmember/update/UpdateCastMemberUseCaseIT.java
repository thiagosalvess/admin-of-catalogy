package com.thiagosalvess.admin.catalogy.application.castmember.update;

import com.thiagosalvess.admin.catalogy.IntegrationTest;
import com.thiagosalvess.admin.catalogy.domain.castmember.CastMember;
import com.thiagosalvess.admin.catalogy.domain.castmember.CastMemberGateway;
import com.thiagosalvess.admin.catalogy.domain.castmember.CastMemberID;
import com.thiagosalvess.admin.catalogy.domain.castmember.CastMemberType;
import com.thiagosalvess.admin.catalogy.domain.exceptions.NotFoundException;
import com.thiagosalvess.admin.catalogy.domain.exceptions.NotificationException;
import com.thiagosalvess.admin.catalogy.infrastructure.castmember.persistence.CastMemberJpaEntity;
import com.thiagosalvess.admin.catalogy.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static com.thiagosalvess.admin.catalogy.domain.Fixture.CastMembers.type;
import static com.thiagosalvess.admin.catalogy.domain.Fixture.name;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@IntegrationTest
public class UpdateCastMemberUseCaseIT {

    @Autowired
    private UpdateCastMemberUseCase useCase;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @SpyBean
    private CastMemberGateway castMemberGateway;

    @Test
    public void givenAValidCommand_whenCallsUpdateCastMember_shouldReturnItsIdentifier() {
        final var aMember = CastMember.newMember("vin diesel", CastMemberType.DIRECTOR);

        this.castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        final var expectedId = aMember.getId();
        final var expectedName = name();
        final var expectedType = CastMemberType.ACTOR;

        final var aCommand = UpdateCastMemberCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedType
        );

        final var actualOutput = useCase.execute(aCommand);

        assertNotNull(actualOutput);
        assertEquals(expectedId.getValue(), actualOutput.id());

        final var actualPersistedMember =
                this.castMemberRepository.findById(expectedId.getValue()).get();

        assertEquals(expectedName, actualPersistedMember.getName());
        assertEquals(expectedType, actualPersistedMember.getType());
        assertEquals(aMember.getCreatedAt(), actualPersistedMember.getCreatedAt());
        assertTrue(aMember.getUpdatedAt().isBefore(actualPersistedMember.getUpdatedAt()));

        verify(castMemberGateway).findById(any());
        verify(castMemberGateway).update(any());
    }

    @Test
    public void givenAnInvalidName_whenCallsUpdateCastMember_shouldThrowsNotificationException() {
        final var aMember = CastMember.newMember("vin diesel", CastMemberType.DIRECTOR);

        this.castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        final var expectedId = aMember.getId();
        final String expectedName = null;
        final var expectedType = CastMemberType.ACTOR;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var aCommand = UpdateCastMemberCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedType
        );

        final var actualException = assertThrows(NotificationException.class, () -> {
            useCase.execute(aCommand);
        });

        assertNotNull(actualException);

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(castMemberGateway).findById(any());
        verify(castMemberGateway, times(0)).update(any());
    }

    @Test
    public void givenAnInvalidType_whenCallsUpdateCastMember_shouldThrowsNotificationException() {
        final var aMember = CastMember.newMember("vin diesel", CastMemberType.DIRECTOR);

        this.castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        final var expectedId = aMember.getId();
        final var expectedName = name();
        final CastMemberType expectedType = null;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'type' should not be null";

        final var aCommand = UpdateCastMemberCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedType
        );

        final var actualException = assertThrows(NotificationException.class, () -> {
            useCase.execute(aCommand);
        });

        assertNotNull(actualException);

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(castMemberGateway).findById(any());
        verify(castMemberGateway, times(0)).update(any());
    }

    @Test
    public void givenAnInvalidId_whenCallsUpdateCastMember_shouldThrowsNotFoundException() {
        final var expectedId = CastMemberID.from("123");
        final var expectedName = name();
        final var expectedType = type();

        final var expectedErrorMessage = "CastMember with ID 123 was not found";

        final var aCommand = UpdateCastMemberCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedType
        );

        final var actualException = assertThrows(NotFoundException.class, () -> {
            useCase.execute(aCommand);
        });

        assertNotNull(actualException);

        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(castMemberGateway).findById(any());
        verify(castMemberGateway, times(0)).update(any());
    }
}
