package nl.abnamro.culinarycompass.repository;

import nl.abnamro.culinarycompass.model.Recipe;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RecipeRepository extends MongoRepository<Recipe, String> {



}
