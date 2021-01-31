package ru.pugart.vaccineavailabilitytracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.pugart.vaccineavailabilitytracker.service.ParserManager;

@SpringBootApplication
public class VaccineAvailabilityTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(VaccineAvailabilityTrackerApplication.class, args);
        new ParserManager().getLocations();
    }

}
