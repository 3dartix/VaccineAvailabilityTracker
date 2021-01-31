package ru.pugart.vaccineavailabilitytracker.utils;

import lombok.extern.slf4j.Slf4j;
import ru.pugart.vaccineavailabilitytracker.dto.Location;

import java.time.LocalDate;
import java.util.Arrays;

@Slf4j
public class LocationBuilder {

    private Location location;

    public LocationBuilder() {
        location = new Location();
    }

    public LocationBuilder setStartDate(Object startDate){
        if (startDate instanceof String){
            location.setStartDate(parseDate((String)startDate));
        }

        if (startDate instanceof LocalDate){
            location.setStartDate((LocalDate) startDate);
        }

        return this;
    }

    public LocationBuilder setEndDate(Object startDate){
        if (startDate instanceof String){
            location.setEndDate(parseDate((String)startDate));
        }

        if (startDate instanceof LocalDate){
            location.setEndDate((LocalDate) startDate);
        }

        return this;
    }

    public LocationBuilder setName(String name){
        location.setName(name);
        return this;
    }

    public LocationBuilder setPhone(String phone){
        location.setPhone(phone);
        return this;
    }

    public LocationBuilder setAddresses(String...addresses){
        location.setAddresses(Arrays.asList(addresses));
        return this;
    }

    public Location build(){
        return location;
    }

    private LocalDate parseDate(String date){
        LocalDate localDate = null;

        // плагины
        localDate = plugin_mos_ru(date);

        return localDate;
    }
    private LocalDate plugin_mos_ru (String startDate){
        Integer day = null;
        Integer month = null;

        String[] arr = startDate.split(" ");

        try {
            day = Integer.parseInt(arr[1]);
        } catch (NumberFormatException ex) {
            log.error("Parse day exception: {}", ex.getLocalizedMessage());
        }

        try {
            month = getMonth(arr[2]);
        } catch (NumberFormatException ex) {
            log.error("Parse month exception: {}", ex.getLocalizedMessage());
        }

        try {
            return LocalDate.of(2021, month, day);
        } catch (NumberFormatException ex) {
            log.error("Create date exception: {}", ex.getLocalizedMessage());
            return null;
        }
    }

    private int getMonth(String month){
        if(month.toLowerCase().startsWith("январ")){
            return 1;
        } else if (month.toLowerCase().startsWith("феврал")){
            return 2;
        } else if (month.toLowerCase().startsWith("март") && month.length() > 3 && month.length() < 7){
            return 3;
        } else if (month.toLowerCase().startsWith("апрел")){
            return 4;
        } else if (month.toLowerCase().startsWith("ма") && month.length() < 4){
            return 5;
        } else if (month.toLowerCase().startsWith("июн")){
            return 6;
        } else if (month.toLowerCase().startsWith("июл")){
            return 7;
        } else if (month.toLowerCase().startsWith("август")){
            return 8;
        } else if (month.toLowerCase().startsWith("сентябр")){
            return 9;
        } else if (month.toLowerCase().startsWith("октябр")){
            return 10;
        } else if (month.toLowerCase().startsWith("ноябр")){
            return 11;
        } else if (month.toLowerCase().startsWith("декабр")){
            return 12;
        } else {
            throw new NumberFormatException();
        }
    }
}
