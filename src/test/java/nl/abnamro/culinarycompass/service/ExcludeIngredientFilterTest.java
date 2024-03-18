package nl.abnamro.culinarycompass.service;

import nl.abnamro.culinarycompass.service.dto.RecipeFilterDto;
import nl.abnamro.culinarycompass.service.impl.ExcludeIngredientFilter;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExcludeIngredientFilterTest {

    @Test
    void applyFilter_WithExcludedIngredients_ReturnsNonEmptyOptional() {

        RecipeFilterDto recipeFilterDto = new RecipeFilterDto();
        recipeFilterDto.setExcludeIngredientNames(Arrays.asList("Salt", "Pepper"));

        RecipeFilter filter = new ExcludeIngredientFilter();

        Optional<AggregationOperation> result = filter.applyFilter(recipeFilterDto);
        assertTrue(result.isPresent(), "Expected a non-empty Optional for non-empty excluded ingredients list");

    }

    @Test
    void applyFilter_WithNoExcludedIngredients_ReturnsEmptyOptional() {

        RecipeFilterDto recipeFilterDto = new RecipeFilterDto();
        RecipeFilter filter = new ExcludeIngredientFilter();

        Optional<AggregationOperation> result = filter.applyFilter(recipeFilterDto);

        assertFalse(result.isPresent(), "Expected an empty Optional for empty excluded ingredients list");
    }

}
