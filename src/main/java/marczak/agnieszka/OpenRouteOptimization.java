package marczak.agnieszka;

import java.util.ArrayList;
import java.util.List;

public class OpenRouteOptimization {
    private List<Vehicle> vehicles = new ArrayList<>();
    private List<Job> jobs = new ArrayList<>();

    public OpenRouteOptimization() {
    }

    public OpenRouteOptimization(Vehicle vehicle) {
        vehicles.add(vehicle);
    }

    public void addWaypoint(Job waypoint){
        jobs.add(waypoint);
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public List<Job> getJobs() {
        return jobs;
    }
}
