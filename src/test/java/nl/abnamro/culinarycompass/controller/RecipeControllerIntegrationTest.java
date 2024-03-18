package nl.abnamro.culinarycompass.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.abnamro.culinarycompass.model.FoodType;
import nl.abnamro.culinarycompass.service.RecipeService;
import nl.abnamro.culinarycompass.service.dto.IngredientDto;
import nl.abnamro.culinarycompass.service.dto.RecipeDto;
import nl.abnamro.culinarycompass.service.dto.RecipeFilterDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(RecipeController.class)
public class RecipeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecipeService recipeService;

    @Autowired
    private ObjectMapper objectMapper;

    private RecipeDto recipeDto;


    @BeforeEach
    void setUp() {
        recipeDto = new RecipeDto();
        recipeDto.setName("Test Recipe");
        recipeDto.setId(UUID.randomUUID().toString());
        recipeDto.setDescription("Test Description");
        recipeDto.setFoodType(FoodType.VEGAN);
        recipeDto.setServings(4);
        recipeDto.setInstruction("Test Instructions");
        IngredientDto ingredientDto = new IngredientDto();
        ingredientDto.setName("Test Ingredient");
        recipeDto.setIngredients(List.of(ingredientDto));

    }

    @Test
    @WithMockUser(value = "adminn", password = "123456")
    void saveRecipeWithIngredients() throws Exception {

        given(recipeService.save(any(RecipeDto.class))).willReturn(recipeDto);

        mockMvc.perform(post("/recipes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipeDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(recipeDto.getName())))
                .andExpect(jsonPath("$.description", is(recipeDto.getDescription())))
                .andExpect(jsonPath("$.foodType", is(recipeDto.getFoodType().toString())))
                .andExpect(jsonPath("$.servings", is(recipeDto.getServings())))
                .andExpect(jsonPath("$.instruction", is(recipeDto.getInstruction())))
                // For arrays or lists, verify the structure and contents match
                .andExpect(jsonPath("$.ingredients[*].name", containsInAnyOrder(
                        recipeDto.getIngredients().stream().map(IngredientDto::getName).toArray()
                )));

    }

    @Test
    @WithMockUser
    void getAllRecipes() throws Exception {

        Page<RecipeDto> page = new PageImpl<>(Collections.singletonList(recipeDto));
        given(recipeService.findAll(any(Pageable.class))).willReturn(page);

        mockMvc.perform(get("/recipes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name", is(recipeDto.getName())));

    }

    @Test
    @WithMockUser
    void getRecipeById() throws Exception {

        given(recipeService.findById(any(String.class))).willReturn(recipeDto);

        mockMvc.perform(get("/recipes/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(recipeDto.getId())))
                .andExpect(jsonPath("$.name", is(recipeDto.getName())))
                .andExpect(jsonPath("$.description", is(recipeDto.getDescription())))
                .andExpect(jsonPath("$.foodType", is(recipeDto.getFoodType().toString())))
                .andExpect(jsonPath("$.servings", is(recipeDto.getServings())))
                .andExpect(jsonPath("$.instruction", is(recipeDto.getInstruction())))
                .andExpect(jsonPath("$.ingredients", hasSize(1)));

    }


    @Test
    @WithMockUser
    void updateRecipeWithIngredients() throws Exception {

        given(recipeService.update(any(RecipeDto.class))).willReturn(recipeDto);

        mockMvc.perform(put("/recipes/{id}", recipeDto.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipeDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(recipeDto.getName())))
                .andExpect(jsonPath("$.ingredients", hasSize(1)))
                .andExpect(jsonPath("$.ingredients[0].name", is("Test Ingredient")));

    }

    @Test
    @WithMockUser
    void deleteRecipe() throws Exception {

        String randomUUID = UUID.randomUUID().toString();
        doNothing().when(recipeService).delete(any(String.class));

        // Perform a DELETE request and verify the outcome
        mockMvc.perform(delete("/recipes/{id}", randomUUID)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Verify that recipeService.delete() was called once with the correct ID
        verify(recipeService, times(1)).delete(randomUUID);

    }

    @Test
    @WithMockUser
    void filterRecipes() throws Exception {

        given(recipeService.filterRecipes(any(RecipeFilterDto.class))).willReturn(List.of(recipeDto));

        mockMvc.perform(get("/recipes/search")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(recipeDto.getName())));

    }
}
