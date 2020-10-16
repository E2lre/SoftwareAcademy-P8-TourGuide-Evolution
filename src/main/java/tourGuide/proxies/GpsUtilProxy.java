package tourGuide.proxies;

//import gpsUtil.location.Attraction;
import tourGuide.beans.Attraction;
//import gpsUtil.location.VisitedLocation;
import tourGuide.beans.VisitedLocation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "gpsUtil", url="localhost:8082")
public interface GpsUtilProxy {

    @GetMapping(value ="/getUserLocation")
    //VisitedLocation getUserLocation(@RequestParam String userId);
    VisitedLocation getUserLocation(@RequestParam UUID userId);
    //VisitedLocation visitedlocation(@RequestParam String userId);

    @GetMapping(value ="/getAttractions")
    List<Attraction> getAttractions();
    //List<Attraction> attractionList();

}
