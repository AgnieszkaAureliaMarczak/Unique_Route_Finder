package marczak.agnieszka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class HelloController {
    private static final String OPEN_ROUTE_URL = "https://api.openrouteservice.org/v2/directions/cycling-regular/gpx";
    @Value("${key}")
    private  String apiKey;
    public HelloController() {
        System.out.println("Hello Controller");
    }

    @RequestMapping(path = "/api/v0/routes", method = RequestMethod.GET)
    public String welcomeUser(String from, String to) {
        System.out.println(apiKey);
        System.out.println(from);
        System.out.println(to);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", apiKey);
        headers.add("Content-Type", "application/json");
        HttpEntity entity = new HttpEntity<>(String.format("{\"coordinates\":[[%s],[%s]]}", from, to),headers);
        ResponseEntity<String> response = restTemplate.exchange(OPEN_ROUTE_URL, HttpMethod.POST, entity, String.class);
        return response.getBody();
    }

}
