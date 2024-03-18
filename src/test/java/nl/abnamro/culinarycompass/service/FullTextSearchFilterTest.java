package nl.abnamro.culinarycompass.service;

import nl.abnamro.culinarycompass.service.dto.RecipeFilterDto;
import nl.abnamro.culinarycompass.service.impl.FullTextSearchFilter;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FullTextSearchFilterTest {

    @Test
    void applyFilter_WithInstruction_ReturnsNonEmptyOptional() {

        RecipeFilterDto recipeFilterDto = new RecipeFilterDto();
        recipeFilterDto.setInstruction("pasta");
        RecipeFilter filter = new FullTextSearchFilter();
        Optional<AggregationOperation> result = filter.applyFilter(recipeFilterDto);
        assertTrue(result.isPresent(), "Expected a non-empty Optional for non-empty instruction");

    }

    @Test
    void applyFilter_WithoutInstruction_ReturnsEmptyOptional() {

        RecipeFilterDto recipeFilterDto = new RecipeFilterDto();
        RecipeFilter filter = new FullTextSearchFilter();
        Optional<AggregationOperation> result = filter.applyFilter(recipeFilterDto);
        assertFalse(result.isPresent(), "Expected an empty Optional for null or empty instruction");

    }

}
