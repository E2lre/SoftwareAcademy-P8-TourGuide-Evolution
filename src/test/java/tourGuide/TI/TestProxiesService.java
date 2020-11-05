package tourGuide.TI;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import tourGuide.model.external.Attraction;
import tourGuide.model.external.VisitedLocation;
import tourGuide.service.GpsUtilProxyService;
import tourGuide.service.TripPricerProxyService;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TestProxiesService {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TripPricerProxyService tripPricerProxyService;

    @Autowired
    private GpsUtilProxyService gpsUtilProxyService;

    @Test
    public void getProviderName_giveInfo_returnProvider() {
        String tripPricerApiKey = "test-server-api-key";
        String provider = tripPricerProxyService.getProviderName(tripPricerApiKey,1);
        assertNotEquals("", provider);
        assertNotNull(provider.toString());
    }

    @Test
    public void getAttractions_noData_returnAttraction() {

        List<Attraction> attractions = gpsUtilProxyService.getAttractions();
        assertNotNull(attractions);
        assertNotNull(attractions.get(0).toString());
    }
    @Test
    public void getUserLocation_giveUUIDUser_returnVisitedLocation() {

        VisitedLocation visitedLocation = gpsUtilProxyService.getUserLocation(UUID.randomUUID());
        assertNotNull(visitedLocation.toString());
    }

}
