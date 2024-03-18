package nl.abnamro.culinarycompass.repository;

import nl.abnamro.culinarycompass.model.Ingredient;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IngredientRepository extends MongoRepository<Ingredient, String> {



}
