package nl.abnamro.culinarycompass.controller;

import lombok.AllArgsConstructor;
import nl.abnamro.culinarycompass.controller.api.RecipeAPI;
import nl.abnamro.culinarycompass.service.RecipeService;
import nl.abnamro.culinarycompass.service.dto.RecipeDto;
import nl.abnamro.culinarycompass.service.dto.RecipeFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller class for handling recipe-related HTTP requests.
 * Implements the {@link RecipeAPI} interface.
 */
@RestController
@RequestMapping("/recipes")
@AllArgsConstructor
public class RecipeController implements RecipeAPI {

    private RecipeService recipeService;

    /**
     * Retrieves all recipes with pagination support.
     *
     * @param pageNumber the page number (optional)
     * @param pageSize   the page size (optional)
     * @return a response entity containing a page of recipe DTOs
     */
    @GetMapping
    @Override
    public ResponseEntity<Page<RecipeDto>> getAllRecipes(@RequestParam(required = false, defaultValue = "0") Integer pageNumber, @RequestParam(required = false, defaultValue = "10") Integer pageSize) {

        return ResponseEntity.ok(recipeService.findAll(PageRequest.of(pageNumber, pageSize, Sort.by("name"))));

    }

    /**
     * Retrieves a recipe by its ID.
     *
     * @param id the ID of the recipe to retrieve
     * @return a response entity containing the recipe DTO
     */
    @GetMapping("/{id}")
    @Override
    public ResponseEntity<RecipeDto> getRecipeById(@PathVariable String id) {

        return ResponseEntity.ok(recipeService.findById(id));

    }

    /**
     * Creates a new recipe.
     *
     * @param recipeDto the DTO representing the recipe to create
     * @return a response entity containing the saved recipe DTO
     */
    @PostMapping
    @Override
    public ResponseEntity<RecipeDto> createRecipe(@RequestBody RecipeDto recipeDto) {

        return ResponseEntity.ok(recipeService.save(recipeDto));

    }

    /**
     * Updates an existing recipe.
     *
     * @param recipeDto the DTO representing the updated recipe
     * @return a response entity containing the updated recipe DTO
     */
    @PutMapping("/{id}")
    @Override
    public ResponseEntity<RecipeDto> updateRecipe(@RequestBody RecipeDto recipeDto) {

        return ResponseEntity.ok(recipeService.update(recipeDto));

    }


    /**
     * Deletes a recipe by its ID.
     *
     * @param id the ID of the recipe to delete
     * @return a response entity with no content
     */
    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<Void> deleteRecipe(@PathVariable String id) {

        recipeService.delete(id);
        return ResponseEntity.ok().build();

    }

    /**
     * Filters recipes based on the provided filter criteria.
     *
     * @param recipeFilterDto the DTO containing the filter criteria
     * @return a response entity containing a list of recipe DTOs matching the filter criteria
     */
    @GetMapping("/search")
    @Override
    public ResponseEntity<List<RecipeDto>> filterRecipes(@ModelAttribute RecipeFilterDto recipeFilterDto) {

        return ResponseEntity.ok(recipeService.filterRecipes(recipeFilterDto));

    }

}
