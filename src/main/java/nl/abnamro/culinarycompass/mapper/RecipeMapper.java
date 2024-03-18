package nl.abnamro.culinarycompass.mapper;

import nl.abnamro.culinarycompass.model.Recipe;
import nl.abnamro.culinarycompass.service.dto.RecipeDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RecipeMapper {

    Recipe dtoToEntity(RecipeDto dto);

    RecipeDto entityToDto(Recipe entity);

    List<Recipe> dtoToEntity(List<RecipeDto> dto);

    List<RecipeDto> entityToDto(List<Recipe> entity);


}
