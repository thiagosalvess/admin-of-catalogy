package com.thiagosalvess.admin.catalogy.infrastructure.castmember.models;

import com.thiagosalvess.admin.catalogy.JacksonTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import static com.thiagosalvess.admin.catalogy.domain.Fixture.CastMembers.type;
import static com.thiagosalvess.admin.catalogy.domain.Fixture.name;
import static org.assertj.core.api.Assertions.assertThat;

@JacksonTest
public class CreateCastMemberRequestTest {
    @Autowired
    private JacksonTester<CreateCastMemberRequest> json;

    @Test
    public void testUnmarshall() throws Exception {
        final var expectedName = name();
        final var expectedType = type();

        final var json = """
                {
                  "name": "%s",
                  "type": "%s"
                }
                """.formatted(expectedName, expectedType);

        final var actualJson = this.json.parse(json);

        assertThat(actualJson)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("type", expectedType);
    }
}
