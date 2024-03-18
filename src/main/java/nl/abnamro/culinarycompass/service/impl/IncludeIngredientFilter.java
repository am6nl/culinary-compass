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
 * Implements the {@link RecipeFilter} to facilitate inclusion-based filtering of recipes by ingredient names.
 * It constructs a query to fetch recipes that specifically contain any of the ingredients listed in
 * {@link RecipeFilterDto#getIncludeIngredientNames()}.
 */
@Component
@Order(10)
@Log4j2
public class IncludeIngredientFilter implements RecipeFilter {

    /**
     * Applies a filter to include recipes based on specified ingredient names.
     * If {@link RecipeFilterDto#getIncludeIngredientNames()} is provided and not empty,
     * it generates a criteria to filter recipes that include any of the specified ingredients.
     *
     * @param recipeFilterDto Contains the criteria for filtering recipes, including ingredient names to include.
     * @return An {@link Optional<AggregationOperation>} encapsulating the match operation for filtering
     * recipes by the specified ingredients, or an empty Optional if no ingredient names are provided.
     */
    @Override
    public Optional<AggregationOperation> applyFilter(RecipeFilterDto recipeFilterDto) {


        if (recipeFilterDto.getIncludeIngredientNames() != null && !recipeFilterDto.getIncludeIngredientNames().isEmpty()) {
            log.debug("IncludeIngredientFilter applies for {}", recipeFilterDto.getIncludeIngredientNames());
            Criteria criteria = Criteria.where("ingredients.name").in(recipeFilterDto.getIncludeIngredientNames());
            AggregationOperation match = Aggregation.match(criteria);
            return Optional.of(match);
        }
        return Optional.empty();

    }

}
