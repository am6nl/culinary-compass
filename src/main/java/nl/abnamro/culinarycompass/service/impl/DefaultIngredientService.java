package nl.abnamro.culinarycompass.service.impl;

import lombok.AllArgsConstructor;
import nl.abnamro.culinarycompass.exception.NotFoundException;
import nl.abnamro.culinarycompass.mapper.IngredientMapper;
import nl.abnamro.culinarycompass.model.Ingredient;
import nl.abnamro.culinarycompass.repository.IngredientRepository;
import nl.abnamro.culinarycompass.service.IngredientService;
import nl.abnamro.culinarycompass.service.dto.IngredientDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DefaultIngredientService implements IngredientService {

    private final IngredientRepository ingredientRepository;
    private final IngredientMapper ingredientMapper;

    /**
     * Retrieves all ingredients.
     *
     * @return List of IngredientDto objects representing all ingredients
     */
    public List<IngredientDto> findAll() {

        List<Ingredient> ingredients = ingredientRepository.findAll();
        return ingredientMapper.entityToDto(ingredients);

    }

    /**
     * Retrieves an ingredient by its ID.
     *
     * @param id ID of the ingredient to retrieve
     * @return IngredientDto object representing the retrieved ingredient
     * @throws nl.abnamro.culinarycompass.exception.NotFoundException if the ingredient with the specified ID is not found
     */
    public IngredientDto findById(String id) {

        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ingredient not found with ID: " + id));
        return ingredientMapper.entityToDto(ingredient);

    }

    /**
     * Checks if an ingredient exists in the database by its identifier.
     *
     * @param id The unique identifier of the ingredient to check.
     * @return {@code true} if an ingredient with the given id exists, otherwise {@code false}.
     */
    @Override
    public boolean existById(String id) {
        return ingredientRepository.existsById(id);
    }

    /**
     * Saves a given ingredient to the database. If the ingredient is new, it will be created;
     * otherwise, the existing ingredient will be updated with the provided details.
     *
     * @param ingredientDto The ingredient data transfer object containing the ingredient details to save.
     * @return An {@link IngredientDto} representing the saved or updated ingredient.
     */
    @Override
    public IngredientDto save(IngredientDto ingredientDto) {
        return ingredientMapper.entityToDto(ingredientRepository.save(ingredientMapper.dtoToEntity(ingredientDto)));
    }
}
