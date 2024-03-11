package io.github.hrashk.task.tracker;

import io.github.hrashk.task.tracker.util.DataSeeder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MongoDBContainer;

@TestConfiguration(proxyBeanMethods = false)
@Import(DataSeeder.class)
public class TestTaskTrackerApplication {

    @Bean
    @ServiceConnection
    MongoDBContainer mongoDbContainer() {
        return new MongoDBContainer("mongo:7.0");
    }

    @Bean
    CommandLineRunner seedSampleData(DataSeeder generator) {
        return args -> generator.seed(5);
    }

    public static void main(String[] args) {
        SpringApplication.from(TaskTrackerApplication::main).with(TestTaskTrackerApplication.class).run(args);
    }
}
