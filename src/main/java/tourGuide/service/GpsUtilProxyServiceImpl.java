package tourGuide.service;


import tourGuide.model.external.Attraction;
import tourGuide.model.external.VisitedLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourGuide.proxies.GpsUtilProxy;

import java.util.List;
import java.util.UUID;

/**
 * see GpsUtilProxy for more information
 */
@Service
public class GpsUtilProxyServiceImpl implements GpsUtilProxyService {
    @Autowired
    private  GpsUtilProxy gpsUtilProxy;
    @Override
    public VisitedLocation getUserLocation(UUID userId){
        VisitedLocation visitedLocation = gpsUtilProxy.getUserLocation(userId) ;
        return visitedLocation;
    }
    @Override
    public List<Attraction> getAttractions(){
        return  gpsUtilProxy.getAttractions();
    }
}
