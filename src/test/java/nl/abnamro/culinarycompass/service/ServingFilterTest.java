package nl.abnamro.culinarycompass.service;

import nl.abnamro.culinarycompass.service.dto.RecipeFilterDto;
import nl.abnamro.culinarycompass.service.impl.ServingFilter;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ServingFilterTest {

    @Test
    void applyFilter_WithServings_ReturnsNonEmptyOptional() {

        RecipeFilterDto recipeFilterDto = new RecipeFilterDto();
        recipeFilterDto.setServings(4);
        RecipeFilter filter = new ServingFilter();
        Optional<AggregationOperation> result = filter.applyFilter(recipeFilterDto);
        assertTrue(result.isPresent(), "Expected a non-empty Optional for specified servings");

    }

    @Test
    void applyFilter_WithoutServings_ReturnsEmptyOptional() {

        RecipeFilterDto recipeFilterDto = new RecipeFilterDto();
        RecipeFilter filter = new ServingFilter();
        Optional<AggregationOperation> result = filter.applyFilter(recipeFilterDto);
        assertFalse(result.isPresent(), "Expected an empty Optional for null servings");

    }

}
