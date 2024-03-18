package nl.abnamro.culinarycompass.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import nl.abnamro.culinarycompass.exception.ErrorResponse;
import nl.abnamro.culinarycompass.service.dto.RecipeDto;
import nl.abnamro.culinarycompass.service.dto.RecipeFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Recipes", description = "Endpoints related to recipe management")
public interface RecipeAPI {

    @Operation(summary = "Get all recipes",
            responses = {
                    @ApiResponse(description = "Recipes list",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = RecipeDto.class)))),
                    @ApiResponse(responseCode = "404", description = "No recipe found",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    ResponseEntity<Page<RecipeDto>> getAllRecipes(Integer pageNumber, Integer pageSize);

    @Operation(summary = "Get a recipe by its id",
            responses = {
                    @ApiResponse(description = "The recipe",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = RecipeDto.class))),
                    @ApiResponse(responseCode = "404", description = "Recipe not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    ResponseEntity<RecipeDto> getRecipeById(String id);

    @Operation(summary = "Create a new recipe",
            responses = {
                    @ApiResponse(description = "The created recipe",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = RecipeDto.class)))
            })
    ResponseEntity<RecipeDto> createRecipe(RecipeDto recipe);

    @Operation(summary = "Update a recipe",
            responses = {
                    @ApiResponse(description = "The updated recipe",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = RecipeDto.class)))
            })
    ResponseEntity<RecipeDto> updateRecipe(RecipeDto recipe);

    @Operation(summary = "Delete a recipe")
    public ResponseEntity<Void> deleteRecipe(String id);

    @Operation(summary = "Filter recipes",
            description = "Filter recipes by vegetarian, servings, ingredients include/exclude, and text in instructions",
            responses = {
                    @ApiResponse(description = "The filtered recipes",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = RecipeDto.class))))
            })
    ResponseEntity<List<RecipeDto>> filterRecipes(RecipeFilterDto recipeFilterDto);

}
