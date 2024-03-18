package nl.abnamro.culinarycompass.mapper;

import nl.abnamro.culinarycompass.model.Ingredient;
import nl.abnamro.culinarycompass.service.dto.IngredientDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IngredientMapper {

    Ingredient dtoToEntity(IngredientDto dto);

    IngredientDto entityToDto(Ingredient entity);

    List<Ingredient> dtoToEntity(List<IngredientDto> dto);

    List<IngredientDto> entityToDto(List<Ingredient> entity);

}
