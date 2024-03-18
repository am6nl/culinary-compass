package nl.abnamro.culinarycompass.config;

import liquibase.Liquibase;
import liquibase.database.DatabaseFactory;
import liquibase.ext.mongodb.database.MongoLiquibaseDatabase;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"dev", "prod"})
public class LiquibaseConfig {

    @Value("${spring.data.mongodb.uri}")
    private String mongoUrl;

    @Bean
    CommandLineRunner liquibaseRunner() {

        return args -> {
            try {
                // Adjust the connection details accordingly
                MongoLiquibaseDatabase database = (MongoLiquibaseDatabase) DatabaseFactory.getInstance().openDatabase(mongoUrl, null, null, null, null);
                Liquibase liquibase = new Liquibase("db/changelog/db.changelog-dev.xml", new ClassLoaderResourceAccessor(), database);
                liquibase.update("");
            } catch (Exception e) {
                throw new RuntimeException("Failed to initialize MongoDB with Liquibase", e);
            }
        };

    }

}
