package nl.abnamro.culinarycompass.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.abnamro.culinarycompass.model.FoodType;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeDto {

    @Schema(description = "Unique identifier for the recipe")
    private String id;
    @Schema(description = "Name of the recipe")
    private String name;
    @Schema(description = "Description of the recipe")
    private String description;
    @Schema(description = "Food type of the recipe")
    private FoodType foodType;
    @Schema(description = "Number of servings")
    private Integer servings;
    @Schema(description = "Cooking instructions for the recipe")
    private String instruction;
    List<IngredientDto> ingredients;

}
