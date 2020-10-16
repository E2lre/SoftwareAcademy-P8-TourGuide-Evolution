package tourGuide.service;

import org.springframework.web.bind.annotation.RequestParam;
import tourGuide.model.external.Provider;
//import tripPricer.Provider;

import java.util.List;
import java.util.UUID;


public interface TripPricerProxyService {
    public List<Provider> getPrice(String apiKey, UUID attractionId, int adults, int children, int nightsStay, int rewardsPoints);
    public String getProviderName( String apiKey, int adults);
}
