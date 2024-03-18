package nl.abnamro.culinarycompass.controller;


import nl.abnamro.culinarycompass.exception.NotFoundException;
import nl.abnamro.culinarycompass.repository.IngredientRepository;
import nl.abnamro.culinarycompass.service.IngredientService;
import nl.abnamro.culinarycompass.service.dto.IngredientDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IngredientController.class)
public class IngredientControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @MockBean
    private IngredientService ingredientService;


    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        IngredientDto ingredient1 = new IngredientDto("testid1", "Tomato");
        IngredientDto ingredient2 = new IngredientDto("testid2", "Salt");

        when(ingredientService.findAll()).thenReturn(Arrays.asList(ingredient1, ingredient2));
        when(ingredientService.findById("testid1")).thenReturn(ingredient1);
    }

    @Test
    @WithMockUser(username = "adminn")
    public void getAllIngredients_ShouldReturnAllIngredients() throws Exception {
        mockMvc.perform(get("/ingredients")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Tomato"))
                .andExpect(jsonPath("$[1].name").value("Salt"));
    }

    @Test
    @WithMockUser(username = "adminn")
    public void getIngredientById_WhenIngredientExists_ShouldReturnIngredient() throws Exception {
        mockMvc.perform(get("/ingredients/{id}", "testid1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("testid1"))
                .andExpect(jsonPath("$.name").value("Tomato"));
    }

    @Test
    @WithMockUser(username = "adminn")
    public void getIngredientById_WhenIngredientDoesNotExist_ShouldReturnNotFound() throws Exception {
        when(ingredientService.findById(anyString())).thenThrow(new NotFoundException("Ingredient not found"));

        mockMvc.perform(get("/ingredients/{id}", "999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
