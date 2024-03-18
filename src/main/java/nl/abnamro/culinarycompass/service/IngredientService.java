package nl.abnamro.culinarycompass.service;

import nl.abnamro.culinarycompass.service.dto.IngredientDto;

import java.util.List;

public interface IngredientService {

    List<IngredientDto> findAll();
    IngredientDto findById(String id);
    boolean existById(String id);
    IngredientDto save(IngredientDto ingredientDto);

}
