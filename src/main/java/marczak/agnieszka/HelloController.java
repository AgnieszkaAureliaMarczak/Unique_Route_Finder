package marczak.agnieszka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
public class HelloController {
    private static final String GPX_DIRECTIONS_URL = "https://api.openrouteservice.org/v2/directions/cycling-regular/gpx";
    private static final String OPTIMIZATION_URL = "https://api.openrouteservice.org/optimization";
    @Value("${key}")
    private String apiKey;

    public HelloController() {
        System.out.println("Hello Controller");
    }

    @RequestMapping(path = "/api/v0/routes", method = RequestMethod.GET)
    public String welcomeUser(String from, String to) {
        Vehicle vehicle = new Vehicle(mapToCoordinates(from), mapToCoordinates(to));
        //algorytm
        List<String> intermediate = new ArrayList<>();
        // start: intermediate.add("15.6483688,51.9288503");
        intermediate.add("15.202653938313244,51.69120394006204");
        intermediate.add("15.726535666405958, 51.938273610061984");
        intermediate.add("15.776767378585275,51.91044664252096");
        intermediate.add("15.1372899044551,51.73561889957993");
        intermediate.add("15.834796414737399,51.73793284903263");
        intermediate.add("14.82069,52.44000");
        // end: intermediate.add("14.944330677563961,52.55699603953345");
        ObjectMapper objectMapper = new ObjectMapper(); // główny obiekt biblioteki object mapper - tak mozemy recznie budowac jsony

        System.out.println(apiKey);
        System.out.println(from);
        System.out.println(to);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", apiKey);
        headers.add("Content-Type", "application/json");

        // For GPX Directions:
        /*OpenRouteRequest openRouteRequest = new OpenRouteRequest();
        for (String coordinate : intermediate) {
            openRouteRequest.addCoordinate(coordinate);
        }*/

        OpenRouteOptimization openRouteOptimization = new OpenRouteOptimization(vehicle);
        for (int i = 0; i < intermediate.size(); i++) {
            String coordinate = intermediate.get(i);
            openRouteOptimization.addWaypoint(new Job(i + 1, mapToCoordinates(coordinate)));
        }

        HttpEntity<OpenRouteOptimization> entity = new HttpEntity<>(openRouteOptimization, headers);
        //Jackson - serializuje jsony
        //działa na zasadzie refleksji
        ResponseEntity<String> responseEntity = restTemplate.exchange(OPTIMIZATION_URL, HttpMethod.POST, entity, String.class);
        return responseEntity.getBody();
    }

    private double[] mapToCoordinates(String textCoordinates) {
        String[] coordinates = textCoordinates.split(",");
        return new double[]{Double.parseDouble(coordinates[0]), Double.parseDouble(coordinates[1])};
    }

}
