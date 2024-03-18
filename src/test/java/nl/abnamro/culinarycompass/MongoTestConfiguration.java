package nl.abnamro.culinarycompass;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;

@EnableMongoRepositories
@TestConfiguration
public class MongoTestConfiguration {

    public static String mongoUrl;
    @Container
    public static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest").withExposedPorts(27017);
    static {
        mongoDBContainer.start();
        var mappedPort = mongoDBContainer.getMappedPort(27017);
        System.setProperty("mongodb.container.port", String.valueOf(mappedPort));
    }

    @Bean
    public MongoClient mongoClient() {
        // Example for Testcontainers
        MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");
        mongoDBContainer.start();
        mongoUrl = mongoDBContainer.getReplicaSetUrl();
        return MongoClients.create(mongoDBContainer.getReplicaSetUrl());
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), "culinary");
    }

}
