package tourGuide.TI;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import tourGuide.service.TripPricerProxyService;
import static org.junit.Assert.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TestProxiesService {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TripPricerProxyService tripPricerProxyService;

    @Test
    public void getProviderName_giveinfo_returnProvider() {
        String tripPricerApiKey = "test-server-api-key";
        String provider = tripPricerProxyService.getProviderName(tripPricerApiKey,1);
        assertNotEquals("", provider);
    }

}
