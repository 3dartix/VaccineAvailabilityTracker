package ru.pugart.vaccineavailabilitytracker.service.plugins;

import ru.pugart.vaccineavailabilitytracker.dto.Location;
import ru.pugart.vaccineavailabilitytracker.enums.Status;

import java.util.ArrayList;
import java.util.List;


public abstract class Plugin implements Runnable {
    int delay = 2;
    Status state = Status.READY;
    List<Location> locations;
    String pluginName;

    List<Location> start;

    public List<Location> getLocations() {
        return new ArrayList<>(locations);
    }

    public Status getState() {
        return state;
    }

    public void setState(Status state) {
        this.state = state;
    }

    public String getPluginName(){
        return pluginName;
    }
}
