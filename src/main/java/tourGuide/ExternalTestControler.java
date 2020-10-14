/*package tourGuide;


import org.springframework.http.HttpMethod;
//import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;*/
//import org.springframework.web.reactive.function.BodyInserter;
//import org.springframework.web.reactive.function.BodyInserters;
/*import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.LinkedMultiValueMap;*/
//import reactor.util.MultiValueMap;
/*
@RestController
public class ExternalTestControler {

    @GetMapping(value = "getLocation")
    @RequestMapping("/getExternal")
    public String getLocation(@RequestParam String testString) {
        String result = "Vide";
        WebClient webClient = WebClient.create("http://localhost:8081");
        WebClient.UriSpec<WebClient.RequestBodySpec> request = webClient.method(HttpMethod.GET);
        WebClient.RequestBodySpec uri1 = webClient
                .method(HttpMethod.GET)
                .uri("/getAttractionRewardPoints?attractionId=123e4567-e89b-12d3-a456-426614174000&userId=123e4567-e89b-12d3-a456-426614174000");
        result = uri1.exchange()
                .block()
                .bodyToMono(String.class)
                .block();
        return testString + " " + result;
    }
    @GetMapping(value = "getTest")
    @RequestMapping("/getTest")
    public String getTest(@RequestParam String testString) {
        String result = "Vide";
        WebClient webClient = WebClient.create("http://localhost:8081");
        WebClient.UriSpec<WebClient.RequestBodySpec> request = webClient.method(HttpMethod.GET);
        WebClient.RequestBodySpec uri1 = webClient
                .method(HttpMethod.GET)
                .uri("/getTest?attractionId=123e4567-e89b-12d3-a456-426614174000&userId=123e4567-e89b-12d3-a456-426614174000");
        result = uri1.exchange()
                .block()
                .bodyToMono(String.class)
                .block();
        return testString + " " + result;
    }


    @GetMapping(value = "getLocation2")
    @RequestMapping("/getExternal2")
    public String getLocation2(@RequestParam String testString) {
        String result = "Vide";
        WebClient webClient = WebClient.create("http://localhost:8081");
        WebClient.UriSpec<WebClient.RequestBodySpec> request = webClient.method(HttpMethod.GET);
        WebClient.RequestBodySpec uri1 = webClient
                .method(HttpMethod.GET)
                .uri("/getTest");

        LinkedMultiValueMap map = new LinkedMultiValueMap();

        map.add("attractionId", "123e4567-e89b-12d3-a456-426614174000");
        map.add("userId", "123e4567-e89b-12d3-a456-426614174000");


        result = uri1.exchange()
                .block()
                .bodyToMono(String.class)
                .block();
        return testString + " " + result;
    }
}
*/