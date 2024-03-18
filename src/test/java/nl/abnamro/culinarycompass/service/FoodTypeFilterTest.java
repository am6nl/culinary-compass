package nl.abnamro.culinarycompass.service;

import nl.abnamro.culinarycompass.model.FoodType;
import nl.abnamro.culinarycompass.service.dto.RecipeFilterDto;
import nl.abnamro.culinarycompass.service.impl.FoodTypeFilter;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FoodTypeFilterTest {

    @Test
    void applyFilter_WithNonNullFoodType_ReturnsNonEmptyOptional() {
        // Arrange
        RecipeFilterDto recipeFilterDto = new RecipeFilterDto();
        recipeFilterDto.setFoodType(FoodType.VEGETARIAN);

        RecipeFilter filter = new FoodTypeFilter(); // Replace with your actual implementation class

        // Act
        Optional<AggregationOperation> result = filter.applyFilter(recipeFilterDto);

        // Assert
        assertTrue(result.isPresent(), "Expected a non-empty Optional for non-null foodType");

        // Since we cannot directly inspect the MatchOperation's criteria, we limit our assertions to result presence.
    }

    @Test
    void applyFilter_WithNullFoodType_ReturnsEmptyOptional() {
        // Arrange
        RecipeFilterDto recipeFilterDto = new RecipeFilterDto(); // foodType not set
        RecipeFilter filter = new FoodTypeFilter(); // Replace with your actual implementation class

        // Act
        Optional<AggregationOperation> result = filter.applyFilter(recipeFilterDto);

        // Assert
        assertFalse(result.isPresent(), "Expected an empty Optional for null foodType");
    }

}
