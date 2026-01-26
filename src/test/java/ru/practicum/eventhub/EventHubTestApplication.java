package ru.practicum.eventhub;

import org.springframework.boot.SpringApplication;

public class EventHubTestApplication {

    public static void main(String[] args) {
        SpringApplication.from(EventHubApplication::main).with(TestcontainersConfiguration.class)
            .run(args);
    }

}
