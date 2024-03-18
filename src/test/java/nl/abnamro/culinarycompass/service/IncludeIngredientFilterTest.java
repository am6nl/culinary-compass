package nl.abnamro.culinarycompass.service;

import nl.abnamro.culinarycompass.service.dto.RecipeFilterDto;
import nl.abnamro.culinarycompass.service.impl.IncludeIngredientFilter;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IncludeIngredientFilterTest {

    @Test
    void applyFilter_WithIncludeIngredientNames_ReturnsNonEmptyOptional() {

        RecipeFilterDto recipeFilterDto = new RecipeFilterDto();
        recipeFilterDto.setIncludeIngredientNames(List.of("Tomato", "Basil"));
        RecipeFilter filter = new IncludeIngredientFilter();
        Optional<AggregationOperation> result = filter.applyFilter(recipeFilterDto);
        assertTrue(result.isPresent(), "Expected a non-empty Optional for non-empty includeIngredientNames");

    }

    @Test
    void applyFilter_WithoutIncludeIngredientNames_ReturnsEmptyOptional() {

        RecipeFilterDto recipeFilterDto = new RecipeFilterDto();
        RecipeFilter filter = new IncludeIngredientFilter();
        Optional<AggregationOperation> result = filter.applyFilter(recipeFilterDto);
        assertFalse(result.isPresent(), "Expected an empty Optional for null or empty includeIngredientNames");

    }

}
