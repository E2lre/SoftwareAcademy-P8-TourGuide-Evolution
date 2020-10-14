package tourGuide.service;

//import gpsUtil.location.Attraction;
import tourGuide.beans.Location;
import tourGuide.beans.Attraction;
//import gpsUtil.location.VisitedLocation;
import tourGuide.beans.VisitedLocation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface GpsUtilProxyService {

    public VisitedLocation getUserLocation(String userId);

    public List<Attraction> getAttractions();

}
