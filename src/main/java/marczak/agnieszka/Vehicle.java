package marczak.agnieszka;

public class Vehicle {
    private final int id = 1;
    private final String profile = "cycling-regular";
    private double[] start;
    private double[] end;

    public Vehicle() {
    }

    public Vehicle(double[] start, double[] end) {
        this.start = start;
        this.end = end;
    }

    public int getId() {
        return id;
    }

    public String getProfile() {
        return profile;
    }

    public double[] getStart() {
        return start;
    }

    public double[] getEnd() {
        return end;
    }
}
