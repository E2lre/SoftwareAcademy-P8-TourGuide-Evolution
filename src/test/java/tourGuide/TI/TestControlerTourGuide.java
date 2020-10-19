package tourGuide.TI;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RequestParam;
import tourGuide.beans.VisitedLocation;
import tourGuide.helper.InternalTestHelper;
import tourGuide.model.UserPreferenceDTO;
import tourGuide.service.GpsUtilProxyService;
import tourGuide.service.RewardCentralProxyService;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;

import java.util.Locale;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TestControlerTourGuide {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private GpsUtilProxyService gpsUtil;
    @Autowired
    private RewardCentralProxyService rewardCentral;

    private RewardsService rewardsService;
    private TourGuideService tourGuideService;
    private User user;
    private UserPreferenceDTO userPreferenceDTO;
    private String userName;
    private String badUserName;

    @BeforeEach
    private void setUpEach() {
        user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        userPreferenceDTO = new UserPreferenceDTO(1, "US", 0.0d, 1000.0d, 5, 2, 1, 1);
        userName = "internalUser2";
        //userName = "jon";
        badUserName = "internalUser99999999";

/*        Locale.setDefault(Locale.US);
        //RewardsService rewardsService = new RewardsService(gpsUtil, rewardCentral);
        rewardsService = new RewardsService(gpsUtil, rewardCentral);
        InternalTestHelper.setInternalUserNumber(0);
        //TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);
        tourGuideService = new TourGuideService(gpsUtil, rewardsService);
        tourGuideService.addUser(user);
        VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);
        tourGuideService.tracker.stopTracking();*/
    }

    /*------------------------------ Get ------------------------------*/
   // @Test
    public void getUserPreference_existingUserName_UserPreferenceIsDone() throws Exception {
        //GIVEN : for a existing user
        //WHEN call Controller
        //THEN return is OK
        //userName = "internalUser3";
        //InternalTestHelper.setInternalUserNumber(0);
        InternalTestHelper.setInternalUserNumber(1);
        //userName = tourGuideService.getAllUsers().get(0).getUserName();
        mockMvc.perform(get("/getUserPreference?userName=" + userName))
                .andExpect(status().isOk());
        //tourGuideService.tracker.stopTracking();
    }

    @Test
    public void getUserPreference_inexistingUserName_errorIsReturn() throws Exception {
        //GIVEN : for a existing user
        //WHEN call Controller
        //THEN return is OK
        //userName = "internalUser4";
        InternalTestHelper.setInternalUserNumber(0);
        mockMvc.perform(get("/getUserPreference?userName=" + badUserName))
                .andExpect(status().isNotFound());
    }
    /*------------------------------ Get ------------------------------*/
    @Test
    public void getTripDeals_existingUserName_aTripIsDone() throws Exception {
        //InternalTestHelper.setInternalUserNumber(0);
        mockMvc.perform(get("/getTripDeals?userName=" + userName))
                .andExpect(status().isOk());
    }

 /*   @Test
    public void getTripDeals_inexistingUserName_errorIsReturn() throws Exception {
        mockMvc.perform(get("/getTripDeals?userName=" + badUserName))
                .andExpect(status().isNotFound());
    }*/
     /*------------------------------ Get ------------------------------*/
     @Test
     public void getLocation_existingUserName_aLocationIsDone() throws Exception {
         mockMvc.perform(get("/getLocation?userName=" + userName))
                 .andExpect(status().isOk());

    }
    /*------------------------------ Get ------------------------------*/
/*    @Test
    public void getRewards_existingUserName_RewardIsDone() throws Exception {
        //InternalTestHelper.setInternalUserNumber(1);
        mockMvc.perform(get("/getRewards?userName=" + userName))
                .andExpect(status().isOk());
    }*/
    /*------------------------------ Get ------------------------------*/
/*
    @Test
    public void getRewards_inexistingUserName_RewardIsDone() throws Exception {
        //InternalTestHelper.setInternalUserNumber(1);
        mockMvc.perform(get("/getRewards?userName=" + badUserName))
                .andExpect(status().isOk());
    }
*/

    /*------------------------------ Get ------------------------------*/
    @Test
    public void setUserPreference_existingUserName_okdIsDone() throws Exception {
        //InternalTestHelper.setInternalUserNumber(1);
        mockMvc.perform(post("/setUserPreference?userName=" + userName)
                .content(asJsonString(new UserPreferenceDTO(4000,"USD",1d,1d,1,1,1,0)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());
    }

    @Test
    public void setUserPreference_inexistingUserName_errorISReturn() throws Exception {
        //InternalTestHelper.setInternalUserNumber(1);
        mockMvc.perform(post("/setUserPreference?userName=" + badUserName)
                .content(asJsonString(new UserPreferenceDTO(4000,"USD",1d,1d,1,1,1,0)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    /*------------------------------ Get ------------------------------*/
    @Test
    public void getNearbyAttractionsV2_existingUserName_okdIsDone()throws Exception {
        mockMvc.perform(post("/getNearbyAttractions?userName=" + userName))
                .andExpect(status().isOk());
    }
    /*------------------------------ Get ------------------------------*/
    @Test
    public void getAllCurrentLocations_inAllCase_okdIsDone() throws  Exception{
        mockMvc.perform(post("/getAllCurrentLocations"))
                .andExpect(status().isOk());
    }
    /*-------------------------------- Utility methode ----------------------------------*/
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
