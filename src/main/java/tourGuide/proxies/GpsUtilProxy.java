package tourGuide.proxies;

import tourGuide.model.external.Attraction;
import tourGuide.model.external.VisitedLocation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

/**
 * FeignClient interface for GpsUtil proxy on port 8082
 */
@FeignClient(name = "gpsUtil", url="localhost:8082")
public interface GpsUtilProxy {

    /**
     * call GpsUtil proxy for getUserLocation
     * @param userId uuid User
     * @return Visited location class
     */
    @GetMapping(value ="/getUserLocation")
    VisitedLocation getUserLocation(@RequestParam UUID userId);

    /**
     * call GpsUtil proxy for getattraction
     * @return all attractions
     */
    @GetMapping(value ="/getAttractions")
    List<Attraction> getAttractions();

}
