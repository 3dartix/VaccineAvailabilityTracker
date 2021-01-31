package ru.pugart.vaccineavailabilitytracker.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class Location {
    private LocalDate startDate;
    private LocalDate endDate;
    private List<String> addresses;
    private String name;
    private String phone;
}
