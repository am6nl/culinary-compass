package nl.abnamro.culinarycompass.service.impl;

import lombok.extern.log4j.Log4j2;
import nl.abnamro.culinarycompass.service.RecipeFilter;
import nl.abnamro.culinarycompass.service.dto.RecipeFilterDto;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * A component that implements the {@link RecipeFilter} interface to provide full-text search
 * functionality on recipe instructions. It leverages MongoDB's full-text search capabilities
 * to filter recipes based on a text search criterion specified in {@link RecipeFilterDto}.
 * This filter is particularly useful for finding recipes that contain specific keywords
 * or phrases in their instructions.
 */
@Component
@Order(1)
@Log4j2
public class FullTextSearchFilter implements RecipeFilter {

    /**
     * Applies a full-text search filter to the aggregation pipeline based on the instruction
     * text specified in {@link RecipeFilterDto}. If the instruction text is provided, it
     * constructs a criteria for MongoDB's text search and creates a match operation to filter
     * recipes accordingly.
     *
     * @param recipeFilterDto Contains the filter criteria, including the instruction text for full-text search.
     * @return An {@link Optional<AggregationOperation>} containing the match operation for the full-text search
     * on the recipe instructions, or an empty Optional if no instruction text is specified in the filter DTO.
     */
    @Override
    public Optional<AggregationOperation> applyFilter(RecipeFilterDto recipeFilterDto) {

        if (recipeFilterDto.getInstruction() != null && !recipeFilterDto.getInstruction().isEmpty()) {
            log.debug("FullTextSearchFilter applies for {}", recipeFilterDto.getInstruction());
            TextCriteria criteria = TextCriteria.forDefaultLanguage().matching(recipeFilterDto.getInstruction());
            MatchOperation match = Aggregation.match(criteria);
            return Optional.of(match);
        }
        return Optional.empty();

    }

}
