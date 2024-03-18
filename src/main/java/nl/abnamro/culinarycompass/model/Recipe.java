package nl.abnamro.culinarycompass.model;

import com.azure.spring.data.cosmos.core.mapping.Container;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;

import java.util.List;

@Data
@Container(containerName = "recipe")
public class Recipe {

    @Id
    private String id;
    private String name;
    private String description;
    private FoodType foodType;
    private Integer servings;
    @TextIndexed
    private String instruction;
    private List<Ingredient> ingredients;

}
