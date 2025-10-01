package com.thiagosalvess.admin.catalogy.infrastructure.configuration.usecases;

import com.thiagosalvess.admin.catalogy.application.castmember.create.CreateCastMemberUseCase;
import com.thiagosalvess.admin.catalogy.application.castmember.create.DefaultCreateCastMemberUseCase;
import com.thiagosalvess.admin.catalogy.application.castmember.delete.DefaultDeleteCastMemberUseCase;
import com.thiagosalvess.admin.catalogy.application.castmember.delete.DeleteCastMemberUseCase;
import com.thiagosalvess.admin.catalogy.application.castmember.retrieve.get.DefaultGetCastMemberByIdUseCase;
import com.thiagosalvess.admin.catalogy.application.castmember.retrieve.get.GetCastMemberByIdUseCase;
import com.thiagosalvess.admin.catalogy.application.castmember.retrieve.list.DefaultListCastMembersUseCase;
import com.thiagosalvess.admin.catalogy.application.castmember.retrieve.list.ListCastMembersUseCase;
import com.thiagosalvess.admin.catalogy.application.castmember.update.DefaultUpdateCastMemberUseCase;
import com.thiagosalvess.admin.catalogy.application.castmember.update.UpdateCastMemberUseCase;
import com.thiagosalvess.admin.catalogy.domain.castmember.CastMemberGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class CastMemberUseCaseConfig {
    private final CastMemberGateway castMemberGateway;

    public CastMemberUseCaseConfig(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Bean
    public CreateCastMemberUseCase createCastMemberUseCase() {
        return new DefaultCreateCastMemberUseCase(castMemberGateway);
    }

    @Bean
    public DeleteCastMemberUseCase deleteCastMemberUseCase() {
        return new DefaultDeleteCastMemberUseCase(castMemberGateway);
    }

    @Bean
    public GetCastMemberByIdUseCase getCastMemberByIdUseCase() {
        return new DefaultGetCastMemberByIdUseCase(castMemberGateway);
    }

    @Bean
    public ListCastMembersUseCase listCastMembersUseCase() {
        return new DefaultListCastMembersUseCase(castMemberGateway);
    }

    @Bean
    public UpdateCastMemberUseCase updateCastMemberUseCase() {
        return new DefaultUpdateCastMemberUseCase(castMemberGateway);
    }
}
