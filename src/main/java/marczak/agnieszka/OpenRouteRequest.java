package marczak.agnieszka;

import java.util.ArrayList;
import java.util.List;

public class OpenRouteRequest {
    private List<String[]> coordinates = new ArrayList<>();

    //wymagany przez bibliotekę Jackson
    public OpenRouteRequest() {
    }

    public void addCoordinate(String coordinate){
        coordinates.add(coordinate.split(","));
    }

    //gettery wymagane do mapowania na jsony przez Jackson
    public List<String[]> getCoordinates() {
        return coordinates;
    }
}
/*
* {
* "coordinates": ["abc","abc2"]
* }
*
* */
