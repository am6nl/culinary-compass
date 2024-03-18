package nl.abnamro.culinarycompass.model;

import com.azure.spring.data.cosmos.core.mapping.Container;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@Container(containerName = "ingredient")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Ingredient {

    @Id
    private String id;
    private String name;
    private String description;

}
