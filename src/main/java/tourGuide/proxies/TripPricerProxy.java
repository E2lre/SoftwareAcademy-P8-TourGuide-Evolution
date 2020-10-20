package tourGuide.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tourGuide.model.external.Provider;


import java.util.List;
import java.util.UUID;

/**
 * FeignClient interface for tripPricer proxy on port 8084
 */

@FeignClient(name = "tripPricer", url="localhost:8084")
public interface TripPricerProxy {

    /**
     * call tripPricer proxy to get a price for an attraction
     * @param apiKey api Key
     * @param attractionId uuid for attration Id
     * @param adults number of adult
     * @param children number of children
     * @param nightsStay number of night
     * @param rewardsPoints reward points
     * @return list of provider
     */
    @GetMapping(value ="/getPrice")
    List<Provider> getPrice(@RequestParam String apiKey, @RequestParam UUID attractionId, @RequestParam int adults, @RequestParam int children, @RequestParam int nightsStay, @RequestParam int rewardsPoints);

    /**
     * call tripPricer proxy to get the name of a provider
     * @param apiKey api Key
     * @param adults provider id
     * @return Provider name
     */
    @GetMapping(value ="/getProviderName")
    String getProviderName(@RequestParam String apiKey,@RequestParam int adults);
}
