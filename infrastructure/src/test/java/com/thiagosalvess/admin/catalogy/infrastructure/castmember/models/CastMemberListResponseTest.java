package com.thiagosalvess.admin.catalogy.infrastructure.castmember.models;

import com.thiagosalvess.admin.catalogy.JacksonTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.time.Instant;

import static com.thiagosalvess.admin.catalogy.domain.Fixture.CastMembers.type;
import static com.thiagosalvess.admin.catalogy.domain.Fixture.name;
import static org.assertj.core.api.Assertions.assertThat;

@JacksonTest
public class CastMemberListResponseTest {

    @Autowired
    private JacksonTester<CastMemberListResponse> json;

    @Test
    public void testMarshall() throws Exception {
        final var expectedId = "123";
        final var expectedName = name();
        final var expectedType = type().name();
        final var expectedCreatedAt = Instant.now().toString();

        final var response = new CastMemberListResponse(
                expectedId,
                expectedName,
                expectedType,
                expectedCreatedAt
        );

        final var actualJson = this.json.write(response);

        assertThat(actualJson)
                .hasJsonPathValue("$.id", expectedId)
                .hasJsonPathValue("$.name", expectedName)
                .hasJsonPathValue("$.type", expectedType)
                .hasJsonPathValue("$.created_at", expectedCreatedAt);
    }
}
