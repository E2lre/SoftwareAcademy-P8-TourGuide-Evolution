package tourGuide.TI;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import tourGuide.helper.InternalTestHelper;
import tourGuide.model.external.Provider;
import tourGuide.service.RewardCentralService;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.service.TripPricerProxyService;
import tourGuide.user.User;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TestProxiesService {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RewardCentralService rewardsCentralService;
    @Autowired
    private TripPricerProxyService tripPricerProxyService;
    @Test
    public void getAttractionRewardPoints_giveUUID_returnReward() {
        //GIVEN
        int rewardPoint = -1;
        //WHEN
        rewardPoint = rewardsCentralService.getAttractionRewardPoints(UUID.randomUUID(), UUID.randomUUID());
        //THEN
        assertNotEquals(-1, rewardPoint);
    }
    @Test
    public void getProviderName_giveinfo_returnProvider() {
        String tripPricerApiKey = "test-server-api-key";
        String provider = tripPricerProxyService.getProviderName(tripPricerApiKey,1);
        assertNotEquals("", provider);
    }

}
