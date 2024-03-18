package nl.abnamro.culinarycompass.service.impl;

import lombok.extern.log4j.Log4j2;
import nl.abnamro.culinarycompass.service.RecipeFilter;
import nl.abnamro.culinarycompass.service.dto.RecipeFilterDto;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * A component that implements the {@link RecipeFilter} interface to provide filtering
 * functionality for excluding recipes based on specific ingredients. This filter
 * is applied to the aggregation pipeline to remove recipes containing any of the
 * specified ingredients in {@link RecipeFilterDto#getExcludeIngredientNames()}.
 */
@Component
@Order(12)
@Log4j2
public class ExcludeIngredientFilter implements RecipeFilter {

    /**
     * Applies an exclusion filter to the aggregation pipeline based on ingredient names.
     * If {@link RecipeFilterDto#getExcludeIngredientNames()} is not empty, it constructs
     * a criteria to exclude recipes that contain any of the specified ingredients.
     *
     * @param recipeFilterDto Contains the filter criteria, including ingredients to exclude.
     * @return An {@link Optional<AggregationOperation>} containing the match operation
     * to exclude recipes with specified ingredients, or an empty Optional if no
     * exclusion criteria are provided.
     */
    @Override
    public Optional<AggregationOperation> applyFilter(RecipeFilterDto recipeFilterDto) {

        if (recipeFilterDto.getExcludeIngredientNames() != null && !recipeFilterDto.getExcludeIngredientNames().isEmpty()) {
            log.debug("ExcludeIngredientFilter applies for {}", recipeFilterDto.getExcludeIngredientNames());
            Criteria criteria = Criteria.where("ingredients.name").nin(recipeFilterDto.getExcludeIngredientNames());
            AggregationOperation match = Aggregation.match(criteria);
            return Optional.of(match);
        }
        return Optional.empty();

    }

}
