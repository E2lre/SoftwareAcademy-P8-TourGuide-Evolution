package tourGuide;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tourGuide.beans.Attraction;
import tourGuide.service.GpsUtilProxyService;
import tourGuide.user.User;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static org.junit.Assert.assertTrue;

//import org.junit.Test;

@SpringBootTest
//@ExtendWith(SpringExtension.class)
//@EnableAutoConfiguration
//@EnableFeignClients("tourGuide")
//@ContextConfiguration(classes = {PerfConfig.class})
public class TestPerformanceProxy {
    private Logger logger = LoggerFactory.getLogger(TestPerformanceProxy.class);
    /*@Autowired
    private TourGuideService tourGuideService;
    */
    @Autowired
    private GpsUtilProxyService gpsUtilProxyService;

   // @Disabled("Integration")
    @Test
    public void highVolumeTrackLocationNew() {
        Locale.setDefault(Locale.US);
        logger.debug("Start highVolumeTrackLocation");

        List<Attraction> attractionList = gpsUtilProxyService.getAttractions();
        assertTrue(1==1);

        String userName = "internalUser" + 1;
        String phone = "000";
        String email = userName + "@tourGuide.com";
        User user = new User(UUID.randomUUID(), userName, phone, email);
       // tourGuideService.addUser(user);
    }


}
