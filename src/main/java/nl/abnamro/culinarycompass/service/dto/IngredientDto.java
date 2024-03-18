package nl.abnamro.culinarycompass.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientDto {

    @Schema(description = "Unique identifier for the Ingredient")
    private String id;
    @Schema(description = "Ingredient name")
    private String name;

}
