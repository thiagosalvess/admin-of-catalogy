package com.thiagosalvess.admin.catalogy.application.category.delete;

import com.thiagosalvess.admin.catalogy.application.UseCaseTest;
import com.thiagosalvess.admin.catalogy.domain.category.Category;
import com.thiagosalvess.admin.catalogy.domain.category.CategoryGateway;
import com.thiagosalvess.admin.catalogy.domain.category.CategoryID;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class DeleteCategoryUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultDeleteCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(categoryGateway);
    }

    @Test
    public void givenAValidId_whenCallsDeleteCategory_shouldBeOk() {
        final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var expectedId = aCategory.getId();

        doNothing().when(categoryGateway).deleteById(eq(expectedId));

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        verify(categoryGateway, times(1)).deleteById(eq(expectedId));
    }

    @Test
    public void givenInvalidId_whenCallsDeleteCategory_shouldBeOk() {
        final var expectedId = CategoryID.from("123");

        doNothing().when(categoryGateway).deleteById(eq(expectedId));

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        verify(categoryGateway, times(1)).deleteById(eq(expectedId));
    }

    @Test
    public void givenValidId_whenGatewayThrowsException_shouldReturnException() {
        final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var expectedId = aCategory.getId();

        doThrow(new IllegalStateException("Gateway error")).when(categoryGateway).deleteById(eq(expectedId));

        assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));
        verify(categoryGateway, times(1)).deleteById(eq(expectedId));
    }
}
