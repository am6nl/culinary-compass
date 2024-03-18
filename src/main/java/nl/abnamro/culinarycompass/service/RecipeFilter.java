package nl.abnamro.culinarycompass.service;

import nl.abnamro.culinarycompass.service.dto.RecipeFilterDto;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;

import java.util.Optional;

/**
 * Defines a contract for applying filtering logic to Recipe queries.
 * Implementations of this interface provide specific filtering criteria based on a RecipeFilterDto.
 */
public interface RecipeFilter {

    /**
     * Applies a filtering specification to a Recipe query based on the provided RecipeFilterDto.
     *
     * @param recipeFilterDto The data transfer object containing filter criteria.
     * @return A Specification object that can be used to filter Recipe queries.
     */
    Optional<AggregationOperation> applyFilter(RecipeFilterDto recipeFilterDto);

}
