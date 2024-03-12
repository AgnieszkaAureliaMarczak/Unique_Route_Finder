package marczak.agnieszka;

import java.util.ArrayList;
import java.util.List;

public class OpenRouteDirectionsGPX {
    private List<double[]> coordinates = new ArrayList<>();

    //wymagany przez bibliotekę Jackson
    public OpenRouteDirectionsGPX() {
    }

    public void addCoordinate(double[] coordinate){
        coordinates.add(coordinate);
    }

    //gettery wymagane do mapowania na jsony przez Jackson
    public List<double[]> getCoordinates() {
        return coordinates;
    }
}
/*
* {
* "coordinates": ["abc","abc2"]
* }
*
* */
