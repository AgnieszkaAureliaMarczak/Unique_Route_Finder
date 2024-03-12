package marczak.agnieszka;

import com.fasterxml.jackson.databind.JsonNode;
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
import org.springframework.web.servlet.function.ServerRequest;

import java.io.IOException;
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

        System.out.println(apiKey);
        System.out.println(from);
        System.out.println(to);

        HttpHeaders headers = createHeaders();
        Vehicle vehicle = new Vehicle(mapToCoordinates(from), mapToCoordinates(to));
        OpenRouteOptimization openRouteOptimization = addRouteWaypoints(vehicle, intermediate);
        String jsonResponse = getOptimizedRoute(openRouteOptimization, headers);
        List<Integer> jobIds = getOptimizedWaypointIds(jsonResponse);
        List<double[]> optimizedCoordinates = getOptimizedCoordinates(openRouteOptimization, jobIds);
        OpenRouteDirectionsGPX openRouteDirectionsGPX = addRouteWaypoints(optimizedCoordinates);
        return getGPXRoute(openRouteDirectionsGPX, headers);
    }

    private double[] mapToCoordinates(String textCoordinates) {
        String[] coordinates = textCoordinates.split(",");
        return new double[]{Double.parseDouble(coordinates[0]), Double.parseDouble(coordinates[1])};
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", apiKey);
        headers.add("Content-Type", "application/json");
        return headers;
    }

    private OpenRouteOptimization addRouteWaypoints(Vehicle vehicle, List<String> intermediate) {
        OpenRouteOptimization openRouteOptimization = new OpenRouteOptimization(vehicle);
        for (int i = 0; i < intermediate.size(); i++) {
            String coordinate = intermediate.get(i);
            openRouteOptimization.addWaypoint(new Job(i + 1, mapToCoordinates(coordinate)));
        }
        return openRouteOptimization;
    }

    private String getOptimizedRoute(OpenRouteOptimization openRouteOptimization, HttpHeaders headers) {
        HttpEntity<OpenRouteOptimization> entity = new HttpEntity<>(openRouteOptimization, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(OPTIMIZATION_URL, HttpMethod.POST, entity, String.class);
        return responseEntity.getBody();
    }

    private String getGPXRoute(OpenRouteDirectionsGPX openRouteDirectionsGPX, HttpHeaders headers) {
        HttpEntity<OpenRouteDirectionsGPX> entityGPX = new HttpEntity<>(openRouteDirectionsGPX, headers);
        RestTemplate restTemplateGPX = new RestTemplate();
        ResponseEntity<String> responseEntityGPX = restTemplateGPX.exchange(GPX_DIRECTIONS_URL, HttpMethod.POST, entityGPX, String.class);
        return responseEntityGPX.getBody();
    }

    private List<Integer> getOptimizedWaypointIds(String jsonResponse) {
        ObjectMapper objectMapper = new ObjectMapper(); // główny obiekt biblioteki object mapper - tak mozemy recznie budowac jsony
        List<Integer> jobIds = new ArrayList<>();
        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode routeNode = rootNode.path("routes").get(0);
            JsonNode stepsNode = routeNode.path("steps");
            for (JsonNode step : stepsNode) {
                if (step.has("id")) {
                    jobIds.add(step.get("id").intValue());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(jobIds);
        return jobIds;
    }

    private List<double[]> getOptimizedCoordinates(OpenRouteOptimization openRouteOptimization, List<Integer> jobIds) {
        List<double[]> optimizedCoordinates = new ArrayList<>();
        for (int id : jobIds) {
            for (int i = 0; i < openRouteOptimization.getJobs().size(); i++) {
                if (id == openRouteOptimization.getJobs().get(i).getId()) {
                    optimizedCoordinates.add(openRouteOptimization.getJobs().get(i).getLocation());
                }
            }
        }
        return optimizedCoordinates;
    }

    private OpenRouteDirectionsGPX addRouteWaypoints(List<double[]> optimizedCoordinates) {
        OpenRouteDirectionsGPX openRouteDirectionsGPX = new OpenRouteDirectionsGPX();
        for (double[] coordinate : optimizedCoordinates) {
            openRouteDirectionsGPX.addCoordinate(coordinate);
        }
        return openRouteDirectionsGPX;
    }
}
