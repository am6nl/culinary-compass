package nl.abnamro.culinarycompass.service.impl;

import lombok.AllArgsConstructor;
import nl.abnamro.culinarycompass.exception.NotFoundException;
import nl.abnamro.culinarycompass.mapper.IngredientMapper;
import nl.abnamro.culinarycompass.mapper.RecipeMapper;
import nl.abnamro.culinarycompass.model.Recipe;
import nl.abnamro.culinarycompass.repository.RecipeRepository;
import nl.abnamro.culinarycompass.service.RecipeFilter;
import nl.abnamro.culinarycompass.service.RecipeService;
import nl.abnamro.culinarycompass.service.dto.RecipeDto;
import nl.abnamro.culinarycompass.service.dto.RecipeFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * Implements {@link RecipeService} to provide service layer functionality for managing recipes.
 * Utilizes {@link RecipeRepository}, {@link RecipeMapper}, and {@link IngredientMapper} for data access and mapping between
 * entity and DTO layers. Supports operations such as finding, saving, updating, and deleting recipes, as well as
 * applying complex filters to retrieve specific recipes.
 */

@Service
@AllArgsConstructor
public class DefaultRecipeService implements RecipeService {

    private final List<RecipeFilter> recipeFilters;
    private MongoTemplate mongoTemplate;
    private RecipeMapper recipeMapper;
    private RecipeRepository recipeRepository;
    private IngredientMapper ingredientMapper;

    /**
     * Finds all recipes, paginated.
     *
     * @param pageable Pagination information.
     * @return A page of {@link RecipeDto} objects.
     */
    @Override
    public Page<RecipeDto> findAll(Pageable pageable) {

        return recipeRepository.findAll(pageable).map(recipeMapper::entityToDto);

    }

    /**
     * Finds a single recipe by its ID.
     *
     * @param id The ID of the recipe to find.
     * @return The found {@link RecipeDto}.
     * @throws NotFoundException if the recipe with the specified ID does not exist.
     */
    @Override
    public RecipeDto findById(String id) {

        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Recipe not found with ID: " + id));
        return recipeMapper.entityToDto(recipe);

    }

    /**
     * Saves a new recipe or updates an existing one based on the presence of an ID.
     *
     * @param recipeDto The recipe to save.
     * @return The saved {@link RecipeDto}.
     */
    @Override
    public RecipeDto save(RecipeDto recipeDto) {

        Recipe recipe = recipeMapper.dtoToEntity(recipeDto);
        recipe.setId(null);
        Recipe savedRecipe = recipeRepository.save(recipe);
        return recipeMapper.entityToDto(savedRecipe);

    }

    /**
     * Updates an existing recipe identified by the ID in the provided {@link RecipeDto}.
     *
     * @param recipeDto The recipe data to update.
     * @return The updated {@link RecipeDto}.
     * @throws IllegalArgumentException if the provided RecipeDto or its ID is null.
     * @throws NotFoundException if the recipe to update is not found.
     */
    @Override
    public RecipeDto update(RecipeDto recipeDto) {

        if (recipeDto == null || recipeDto.getId() == null) {
            throw new IllegalArgumentException("RecipeDto or its ID cannot be null for update operation.");
        }

        Recipe existingRecipe = recipeRepository.findById(recipeDto.getId())
                .orElseThrow(() -> new NotFoundException("Recipe not found with ID: " + recipeDto.getId()));

        if (recipeDto.getName() != null) existingRecipe.setName(recipeDto.getName());
        if (recipeDto.getDescription() != null) existingRecipe.setDescription(recipeDto.getDescription());
        if (recipeDto.getFoodType() != null) existingRecipe.setFoodType(recipeDto.getFoodType());
        if (recipeDto.getServings() != null) existingRecipe.setServings(recipeDto.getServings());
        if (recipeDto.getInstruction() != null) existingRecipe.setInstruction(recipeDto.getInstruction());

        if (recipeDto.getIngredients() != null) {
            existingRecipe.setIngredients(ingredientMapper.dtoToEntity(recipeDto.getIngredients()));
        }

        Recipe updatedRecipe = recipeRepository.save(existingRecipe);
        return recipeMapper.entityToDto(updatedRecipe);

    }

    /**
     * Deletes a recipe by its ID.
     *
     * @param id The ID of the recipe to delete.
     * @throws NotFoundException if the recipe with the specified ID does not exist.
     */
    @Override
    public void delete(String id) {

        if (!recipeRepository.existsById(id)) {
            throw new NotFoundException("Recipe not found with ID: " + id);
        }
        recipeRepository.deleteById(id);

    }

    /**
     * Filters recipes based on the criteria specified in {@link RecipeFilterDto}.
     *
     * @param recipeFilterDto The criteria to filter recipes.
     * @return A list of {@link RecipeDto} that match the filter criteria.
     */
    @Override
    public List<RecipeDto> filterRecipes(RecipeFilterDto recipeFilterDto) {

        List<AggregationOperation> operations = new ArrayList<>();

        for (RecipeFilter filter : recipeFilters) {
            filter.applyFilter(recipeFilterDto).ifPresent(operations::add);
        }

        Aggregation aggregation = Aggregation.newAggregation(operations);
        List<Recipe> recipes = mongoTemplate.aggregate(aggregation, Recipe.class, Recipe.class).getMappedResults();

        return recipeMapper.entityToDto(recipes);

    }

}
