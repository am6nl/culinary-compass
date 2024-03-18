package nl.abnamro.culinarycompass.service.impl;

import lombok.extern.log4j.Log4j2;
import nl.abnamro.culinarycompass.service.RecipeFilter;
import nl.abnamro.culinarycompass.service.dto.RecipeFilterDto;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * A component implementing the {@link RecipeFilter} to facilitate filtering recipes
 * based on their food type. It uses the {@link RecipeFilterDto} to apply a match
 * operation in an aggregation pipeline, filtering recipes to include only those that
 * match the specified food type.
 */
@Component
@Order(13)
@Log4j2
public class FoodTypeFilter implements RecipeFilter {

    /**
     * Applies a filter based on the food type specified in {@link RecipeFilterDto}.
     * If a food type is provided, it constructs a match operation to filter recipes
     * by this criterion.
     *
     * @param recipeFilterDto Contains the filter criteria, including the food type to filter by.
     * @return An {@link Optional<AggregationOperation>} containing the match operation
     * to filter recipes by the specified food type, or an empty Optional if no food type
     * is specified in the filter DTO.
     */
    @Override
    public Optional<AggregationOperation> applyFilter(RecipeFilterDto recipeFilterDto) {

        if (recipeFilterDto.getFoodType() != null) {
            log.debug("FoodTypeFilter applies for {}", recipeFilterDto.getFoodType());
            MatchOperation match = Aggregation.match(Criteria.where("foodType").is(recipeFilterDto.getFoodType()));
            return Optional.of(match);
        }
        return Optional.empty();

    }

}
