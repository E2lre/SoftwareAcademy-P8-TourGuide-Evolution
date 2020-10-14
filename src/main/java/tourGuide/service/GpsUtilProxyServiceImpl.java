package tourGuide.service;

//import gpsUtil.location.Attraction;
import tourGuide.beans.Attraction;
//import gpsUtil.location.VisitedLocation;
import tourGuide.beans.VisitedLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourGuide.proxies.GpsUtilProxy;

import java.util.List;

@Service
public class GpsUtilProxyServiceImpl implements GpsUtilProxyService {
    @Autowired
    private  GpsUtilProxy gpsUtilProxy;
    @Override
    public VisitedLocation getUserLocation(String userId){
        VisitedLocation visitedLocation = gpsUtilProxy.getUserLocation(userId) ;
        return visitedLocation;
        //return gpsUtilProxy.getUserLocation(userId);
    }
    @Override
    public List<Attraction> getAttractions(){
        return  gpsUtilProxy.getAttractions();
    }


}
