package tourGuide.service;

import tourGuide.model.external.Provider;

import java.util.List;
import java.util.UUID;


public interface TripPricerProxyService {
    public List<Provider> getPrice(String apiKey, UUID attractionId, int adults, int children, int nightsStay, int rewardsPoints);
    public String getProviderName( String apiKey, int adults);
}
