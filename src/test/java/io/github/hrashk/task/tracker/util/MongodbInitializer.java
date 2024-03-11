package io.github.hrashk.task.tracker.util;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.wait.strategy.Wait;

public class MongodbInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    private static final MongoDBContainer MONGO_DB_CONTAINER = new MongoDBContainer("mongo:7.0")
            .waitingFor(Wait.forListeningPort());

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        MONGO_DB_CONTAINER.start();
        TestPropertyValues.of(
                "spring.data.mongodb.uri=" + MONGO_DB_CONTAINER.getReplicaSetUrl("appdatabase")
        ).applyTo(applicationContext.getEnvironment());
    }
}
