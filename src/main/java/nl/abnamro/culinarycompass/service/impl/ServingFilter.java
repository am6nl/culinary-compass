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
 * A component implementing {@link RecipeFilter} to provide filtering functionality based on the number of servings.
 * It allows for the retrieval of recipes that match a specified serving size, enhancing the search capabilities
 * within the recipe management system.
 */
@Component
@Order(11)
@Log4j2
public class ServingFilter implements RecipeFilter {

    /**
     * Applies a filter to select recipes based on the number of servings specified in {@link RecipeFilterDto}.
     * This filter facilitates users in finding recipes that match their serving size requirements.
     *
     * @param recipeFilterDto Contains the filter criteria, including the number of servings to filter by.
     * @return An {@link Optional<AggregationOperation>} containing the match operation for filtering
     * recipes by servings, or an empty Optional if no serving size is specified in the filter DTO.
     */
    @Override
    public Optional<AggregationOperation> applyFilter(RecipeFilterDto recipeFilterDto) {

        if (recipeFilterDto.getServings() != null) {
            log.debug("ServingFilter applies for {}", recipeFilterDto.getServings());
            Criteria criteria = Criteria.where("servings").is(recipeFilterDto.getServings());
            AggregationOperation match = Aggregation.match(criteria);
            return Optional.of(match);
        }
        return Optional.empty();

    }

}
