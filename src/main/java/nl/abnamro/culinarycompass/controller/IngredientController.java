package nl.abnamro.culinarycompass.controller;

import lombok.AllArgsConstructor;
import nl.abnamro.culinarycompass.controller.api.IngredientAPI;
import nl.abnamro.culinarycompass.service.IngredientService;
import nl.abnamro.culinarycompass.service.dto.IngredientDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller class for handling ingredient-related HTTP requests.
 */
@RestController
@RequestMapping("/ingredients")
@AllArgsConstructor
public class IngredientController implements IngredientAPI {

    private final IngredientService ingredientService;

    /**
     * Retrieves all ingredients.
     *
     * @return ResponseEntity containing a list of IngredientDto objects representing all ingredients
     */
    @GetMapping
    @Override
    public ResponseEntity<List<IngredientDto>> getAllIngredients() {

        List<IngredientDto> ingredients = ingredientService.findAll();
        return ResponseEntity.ok(ingredients);

    }

    /**
     * Retrieves an ingredient by its ID.
     *
     * @param id ID of the ingredient to retrieve
     * @return ResponseEntity containing the IngredientDto object representing the retrieved ingredient
     */
    @GetMapping("/{id}")
    @Override
    public ResponseEntity<IngredientDto> getIngredientById(@PathVariable String id) {

        IngredientDto ingredient = ingredientService.findById(id);
        return ResponseEntity.ok(ingredient);

    }

}
