package tourGuide.service;

//import gpsUtil.location.Attraction;
import tourGuide.beans.Attraction;
//import gpsUtil.location.VisitedLocation;
import tourGuide.beans.VisitedLocation;


import java.util.List;
import java.util.UUID;

public interface GpsUtilProxyService {

    //public VisitedLocation getUserLocation(String userId);
    public VisitedLocation getUserLocation(UUID userId);
    public List<Attraction> getAttractions();

}
