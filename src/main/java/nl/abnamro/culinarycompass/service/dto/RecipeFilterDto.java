package nl.abnamro.culinarycompass.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import nl.abnamro.culinarycompass.model.FoodType;

import java.util.List;

@Data
public class RecipeFilterDto {

    @Schema(description = "Food type of the recipe")
    private FoodType foodType;
    @Schema(description = "Number of servings")
    private Integer servings;
    @Schema(description = "List of IDs of ingredients to include in the recipe")
    private List<String> includeIngredientNames;
    @Schema(description = "List of IDs of ingredients to exclude from the recipe")
    private List<String> excludeIngredientNames;
    @Schema(description = "Instruction or keywords for recipe search")
    private String instruction;

}
