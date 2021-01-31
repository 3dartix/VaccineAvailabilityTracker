package ru.pugart.vaccineavailabilitytracker.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.pugart.vaccineavailabilitytracker.config.DriverInitializer;
import ru.pugart.vaccineavailabilitytracker.dto.Location;
import ru.pugart.vaccineavailabilitytracker.enums.Status;
import ru.pugart.vaccineavailabilitytracker.service.plugins.Plugin;
import ru.pugart.vaccineavailabilitytracker.service.plugins.PluginMoscow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EnableScheduling
@Service
@Data
@Slf4j
public class ParserManager {

    List<Plugin> plugins = new ArrayList<>();
    Map<String, List<Location>> locations = new HashMap<>();

    public ParserManager() {
        plugins.add(new PluginMoscow());
    }

    public List<Location> getLocations(){
        ArrayList<Location> result = new ArrayList<>();
        locations.forEach((key, value) -> result.addAll(value));
        return result;
    }

//    @Scheduled(cron = "${parser.start-time}")
    @Scheduled(fixedDelay = 20000)
    public void start(){
        if(!DriverInitializer.isConnected()){
            return;
        }

        plugins.forEach(Runnable::run);
    }

    @Scheduled(fixedDelay = 10000)
    public void checkResult(){
        log.info("проверяем результат");
        plugins.forEach(plugin -> {

            if(plugin.getState() == Status.COMPLETE) {
                locations.put(plugin.getPluginName(), plugin.getLocations());
                log.info("парсинг закончен");
            }

        });
    }
}
