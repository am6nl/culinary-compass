package nl.abnamro.culinarycompass.service;

import liquibase.Liquibase;
import liquibase.database.DatabaseFactory;
import liquibase.exception.LiquibaseException;
import liquibase.ext.mongodb.database.MongoLiquibaseDatabase;
import liquibase.resource.ClassLoaderResourceAccessor;
import nl.abnamro.culinarycompass.MongoTestConfiguration;
import nl.abnamro.culinarycompass.exception.NotFoundException;
import nl.abnamro.culinarycompass.mapper.RecipeMapper;
import nl.abnamro.culinarycompass.model.FoodType;
import nl.abnamro.culinarycompass.model.Ingredient;
import nl.abnamro.culinarycompass.model.Recipe;
import nl.abnamro.culinarycompass.repository.RecipeRepository;
import nl.abnamro.culinarycompass.service.dto.IngredientDto;
import nl.abnamro.culinarycompass.service.dto.RecipeDto;
import nl.abnamro.culinarycompass.service.dto.RecipeFilterDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.TextIndexDefinition;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = MongoTestConfiguration.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
public class DefaultRecipeServiceIntegrationTest {

    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    protected RecipeService recipeService;
    @Autowired
    protected RecipeMapper recipeMapper;
    @Autowired
    private RecipeRepository recipeRepository;

    @BeforeAll
    public void setup() throws LiquibaseException {
        mongoTemplate.indexOps(Recipe.class).ensureIndex(new TextIndexDefinition.TextIndexDefinitionBuilder()
                .onField("instruction")
                .build());
        MongoLiquibaseDatabase database = (MongoLiquibaseDatabase) DatabaseFactory.getInstance().openDatabase(MongoTestConfiguration.mongoUrl, null, null, null, null);
        Liquibase liquibase = new Liquibase("db/changelog/db.changelog-dev.xml", new ClassLoaderResourceAccessor(), database);
        liquibase.update("");

    }

    private Recipe createRecipe(String name, FoodType foodType, int servings, String description, List<String> ingredients, String instruction) {
        Recipe recipe = new Recipe();
        recipe.setName(name);
        recipe.setFoodType(foodType);
        recipe.setServings(servings);
        recipe.setDescription(description);
        recipe.setInstruction(instruction);
        recipe.setIngredients(ingredients.stream().map(i -> Ingredient.builder().name(i).build()).toList());

        RecipeDto savedDto = recipeService.save(recipeMapper.entityToDto(recipe));

        return recipeMapper.dtoToEntity(savedDto);
    }

    @Test
    @Order(10)
    void saveRecipeDto_ReturnsSavedRecipe() {

        createRecipe("Spaghetti Carbonara", FoodType.NON_VEGETARIAN, 4, "Creamy pasta", List.of("Pasta", "Egg", "Cheese"), "Oven is required");
        createRecipe("Mushroom Risotto", FoodType.VEGETARIAN, 3, "Rich and creamy risotto", List.of("Rice", "Mushrooms", "Parmesan"), "Stove is required");

        // Verify that the recipes are saved
        List<Recipe> recipes = recipeRepository.findAll();
        assertThat(recipes).hasSize(2);

        Recipe carbonara = recipes.stream()
                .filter(r -> "Spaghetti Carbonara".equals(r.getName()))
                .findFirst()
                .orElse(null);

        Recipe risotto = recipes.stream()
                .filter(r -> "Mushroom Risotto".equals(r.getName()))
                .findFirst()
                .orElse(null);

        // Assertions for Spaghetti Carbonara
        assertThat(carbonara).isNotNull();
        assertThat(carbonara.getFoodType()).isEqualTo(FoodType.NON_VEGETARIAN);
        assertThat(carbonara.getServings()).isEqualTo(4);
        assertThat(carbonara.getIngredients().stream().map(Ingredient::getName).collect(Collectors.toList())).containsExactlyInAnyOrder("Pasta", "Egg", "Cheese");
        assertThat(carbonara.getInstruction()).isEqualTo("Oven is required");

        // Assertions for Mushroom Risotto
        assertThat(risotto).isNotNull();
        assertThat(risotto.getFoodType()).isEqualTo(FoodType.VEGETARIAN);
        assertThat(risotto.getServings()).isEqualTo(3);
        assertThat(risotto.getIngredients().stream().map(Ingredient::getName).collect(Collectors.toList())).containsExactlyInAnyOrder("Rice", "Mushrooms", "Parmesan");
        assertThat(risotto.getInstruction()).isEqualTo("Stove is required");

    }

    @Test
    @Order(20)
    public void whenFilterByFoodTypeAndIngredient_thenCorrectRecipesReturned() {

        RecipeFilterDto filterDto = new RecipeFilterDto();
        filterDto.setFoodType(FoodType.NON_VEGETARIAN);

        List<RecipeDto> filteredRecipes = recipeService.filterRecipes(filterDto);

        assertThat(filteredRecipes).hasSize(1);
        assertThat(filteredRecipes.get(0).getName()).isEqualTo("Spaghetti Carbonara");

    }

    @Test
    @Order(30)
    public void whenFilterByInstructionKeyword_thenCorrectRecipesReturned() {

        RecipeFilterDto filterDto = new RecipeFilterDto();
        filterDto.setInstruction("oven");

        List<RecipeDto> filteredRecipes = recipeService.filterRecipes(filterDto);

        assertThat(filteredRecipes).hasSize(1);
        assertThat(filteredRecipes.get(0).getName()).isEqualTo("Spaghetti Carbonara");

    }

    @Test
    @Order(40)
    public void whenFilterByIncludeIngredient_thenCorrectRecipesReturned() {

        RecipeFilterDto filterDto = new RecipeFilterDto();
        filterDto.setIncludeIngredientNames(List.of("Pasta"));
        List<RecipeDto> filteredRecipes = recipeService.filterRecipes(filterDto);
        assertThat(filteredRecipes).hasSize(1);
        assertThat(filteredRecipes.get(0).getName()).isEqualTo("Spaghetti Carbonara");

    }

    @Test
    @Order(50)
    public void whenFilterByExcludeIngredient_thenCorrectRecipesReturned() {

        RecipeFilterDto filterDto = new RecipeFilterDto();
        filterDto.setExcludeIngredientNames(List.of("Pasta"));
        List<RecipeDto> filteredRecipes = recipeService.filterRecipes(filterDto);
        assertThat(filteredRecipes).hasSize(1);
        assertThat(filteredRecipes.get(0).getName()).isEqualTo("Mushroom Risotto");

    }

    @Test
    @Order(60)
    public void whenFilterByServing_thenCorrectRecipesReturned() {

        RecipeFilterDto filterDto = new RecipeFilterDto();
        filterDto.setServings(3);
        List<RecipeDto> filteredRecipes = recipeService.filterRecipes(filterDto);
        assertThat(filteredRecipes).hasSize(1);
        assertThat(filteredRecipes.get(0).getName()).isEqualTo("Mushroom Risotto");

    }

    
    @Test
    @Order(70)
    public void whenFilterByAllFilters_thenCorrectRecipesReturned() {

        RecipeFilterDto filterDto = new RecipeFilterDto();

        filterDto.setServings(3);
        filterDto.setFoodType(FoodType.VEGETARIAN);
        filterDto.setIncludeIngredientNames(List.of("Rice"));
        filterDto.setExcludeIngredientNames(List.of("Pasta"));
        filterDto.setInstruction("stove");

        List<RecipeDto> filteredRecipes = recipeService.filterRecipes(filterDto);

        assertThat(filteredRecipes).hasSize(1);
        assertThat(filteredRecipes.get(0).getName()).isEqualTo("Mushroom Risotto");

    }

    @Test
    @Order(80)
    void updateRecipe_UpdatesAndReturnsUpdatedRecipeWithAllFields() {

        // Given an existing recipe in the database
        Recipe existingRecipe = new Recipe();
        existingRecipe.setName("Original Recipe");
        existingRecipe.setDescription("Original Description");
        existingRecipe.setFoodType(FoodType.NON_VEGETARIAN);
        existingRecipe.setServings(2);
        existingRecipe.setInstruction("Original Instruction");
        existingRecipe.setIngredients(List.of(Ingredient.builder().name("test").description("test desc").build()));
        recipeRepository.save(existingRecipe);

        // When updating the recipe with new details
        RecipeDto updateDto = new RecipeDto();
        updateDto.setId(existingRecipe.getId()); // Use the saved recipe's ID
        updateDto.setName("Updated Recipe");
        updateDto.setDescription("Updated Description");
        updateDto.setFoodType(FoodType.VEGETARIAN); // Assuming FoodType is an enum or similar
        updateDto.setServings(4);
        updateDto.setInstruction("Updated Instruction");
        updateDto.setIngredients(List.of(IngredientDto.builder().name("updated ingredient").build()));

        RecipeDto updatedDto = recipeService.update(updateDto);

        // Then verify the returned DTO has the updated information
        assertThat(updatedDto.getName()).isEqualTo(updateDto.getName());
        assertThat(updatedDto.getDescription()).isEqualTo(updateDto.getDescription());
        assertThat(updatedDto.getFoodType()).isEqualTo(updateDto.getFoodType());
        assertThat(updatedDto.getServings()).isEqualTo(updateDto.getServings());
        assertThat(updatedDto.getInstruction()).isEqualTo(updateDto.getInstruction());
        assertThat(updatedDto.getIngredients()).containsExactlyElementsOf(updateDto.getIngredients());

        // And verify the recipe is updated in the database
        Recipe updatedRecipe = recipeRepository.findById(existingRecipe.getId()).orElseThrow();
        assertThat(updatedRecipe.getName()).isEqualTo(updateDto.getName());
        assertThat(updatedRecipe.getDescription()).isEqualTo(updateDto.getDescription());
        assertThat(updatedRecipe.getFoodType()).isEqualTo(updateDto.getFoodType());
        assertThat(updatedRecipe.getServings()).isEqualTo(updateDto.getServings());
        assertThat(updatedRecipe.getInstruction()).isEqualTo(updateDto.getInstruction());
        assertThat(updatedRecipe.getIngredients()).containsExactlyElementsOf(List.of(Ingredient.builder().name(updateDto.getIngredients().get(0).getName()).build()));

        recipeRepository.deleteById(updatedRecipe.getId());
    }

    @Test
    void updateRecipe_WithNullDto_ThrowsIllegalArgumentException() {

        assertThrows(IllegalArgumentException.class, () -> recipeService.update(null));

    }

    @Test
    void updateRecipe_WithNullId_ThrowsIllegalArgumentException() {

        RecipeDto updateDto = new RecipeDto(); // Missing ID
        assertThrows(IllegalArgumentException.class, () -> recipeService.update(updateDto));

    }

    @Test
    void updateRecipe_WithNonExistentId_ThrowsNotFoundException() {

        RecipeDto updateDto = new RecipeDto();
        updateDto.setId("nonexistentid"); // An ID that does not exist in the database
        assertThrows(NotFoundException.class, () -> recipeService.update(updateDto));

    }

}
