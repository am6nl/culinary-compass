package nl.abnamro.culinarycompass.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import nl.abnamro.culinarycompass.exception.ErrorResponse;
import nl.abnamro.culinarycompass.service.dto.IngredientDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Ingredients", description = "Endpoints related to ingredient management")
public interface IngredientAPI {

    @Operation(summary = "Get all ingredients")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of ingredients retrieved successfully")
    })
    ResponseEntity<List<IngredientDto>> getAllIngredients();


    @Operation(summary = "Get an ingredient by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ingredient found successfully"),
            @ApiResponse(responseCode = "404", description = "Ingredient not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<IngredientDto> getIngredientById(
            @Parameter(description = "ID of the ingredient to be retrieved") String id);

}
