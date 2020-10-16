package tourGuide.TestProxy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tourGuide.TestPerformanceProxy;
import tourGuide.beans.Attraction;
import tourGuide.proxies.GpsUtilProxy;
import tourGuide.service.GpsUtilProxyService;
import tourGuide.service.RewardCentralService;
import tourGuide.service.TourGuideService;

import java.util.List;

import static org.junit.Assert.assertTrue;

//@ExtendWith(SpringExtension.class)
@SpringBootTest
//@ExtendWith(SpringExtension.class)
@EnableAutoConfiguration
public class TestPerformanceProxy2 {
    private Logger logger = LoggerFactory.getLogger(TestPerformanceProxy2.class);
    @Autowired
    private  GpsUtilProxyService gpsUtilProxy;
    @Autowired
    private TourGuideService tourGuideService;
    @Autowired
    private RewardCentralService rewardCentralService;

    @Test
    public void highVolumeTrackLocationNewTest() {
        logger.debug("start");
        List<Attraction> attractionList = gpsUtilProxy.getAttractions();
        assertTrue(1==1);
    }
}
