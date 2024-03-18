package nl.abnamro.culinarycompass.service;

import nl.abnamro.culinarycompass.service.dto.RecipeDto;
import nl.abnamro.culinarycompass.service.dto.RecipeFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface RecipeService {

    Page<RecipeDto> findAll(Pageable pageable);
    RecipeDto findById(String id);
    RecipeDto save(RecipeDto recipeDto);
    RecipeDto update(RecipeDto recipeDto);
    void delete(String id);
    List<RecipeDto> filterRecipes(RecipeFilterDto recipeFilterDto);

}
