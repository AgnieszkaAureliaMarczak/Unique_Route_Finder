package marczak.agnieszka;

public class Job {
    private int id;
    private double[] location;

    public Job() {
    }

    public Job(int id, double[] location) {
        this.id = id;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public double[] getLocation() {
        return location;
    }
}
