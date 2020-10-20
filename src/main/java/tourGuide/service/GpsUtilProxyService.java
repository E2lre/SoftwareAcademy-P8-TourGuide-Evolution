package tourGuide.service;

import tourGuide.model.external.Attraction;
import tourGuide.model.external.VisitedLocation;

import java.util.List;
import java.util.UUID;

public interface GpsUtilProxyService {

    public VisitedLocation getUserLocation(UUID userId);
    public List<Attraction> getAttractions();

}
