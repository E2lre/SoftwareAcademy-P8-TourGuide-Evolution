package tourGuide.TU;

//import org.junit.Ignore;
//import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import tourGuide.model.external.Location;
import tourGuide.model.external.VisitedLocation;
import tourGuide.model.UserPreferenceDTO;
import tourGuide.model.external.Attraction;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;
import tourGuide.user.UserReward;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TestTourGuideControlerTU {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private TourGuideService tourGuideService;

    private User user;
    private UserPreferenceDTO userPreferenceDTO;
    private String userName;

    @BeforeEach
    private void setUpEach() {
        user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        userPreferenceDTO =new UserPreferenceDTO(1,"US",0.0d,1000.0d,5,2,1,1);
        userName = "internalUser2";
    }
    /*---------------------------------------- Get-------------------------------*/

    @Test
    public void getUserPreference_existingUserName_UserPreferenceIsDone() throws Exception{
         Mockito.when(tourGuideService.getUser(anyString())).thenReturn(user);
        Mockito.when(tourGuideService.getUserPreference(anyString())).thenReturn(userPreferenceDTO);
        //WHEN //THEN return the user préference
        mockMvc.perform(get("/getUserPreference?userName="+user))
                .andExpect(status().isOk());
    }
    /*------------------------------ Get ------------------------------*/
    @Test
    public void getRewards_existingUserName_RewardIsDone() throws Exception {


        String userId="019b04a9-067a-4c76-8817-ee75088c3822";
        String sDate="31/12/1998";
        Double longitude = -48.188821;
        Double latitude  = 74.84371;
        Date testDate=new SimpleDateFormat("dd/MM/yyyy").parse(sDate);
        List<UserReward> userRewards = new ArrayList<>();
        Location location = new Location(longitude,latitude);
        VisitedLocation visitedLocation = new VisitedLocation( UUID.fromString(userId),location,testDate);
        //Initialise Attraction
        Attraction attraction = new Attraction("Tour Effeil", "Paris", "France", latitude,longitude);

        //Initialise userReward list
        UserReward userReward = new UserReward(visitedLocation,attraction);
        userRewards.add(userReward);
        Mockito.when(tourGuideService.getUserRewards(any(User.class))).thenReturn(userRewards);
        mockMvc.perform(get("/getRewards?userName=" + user))
                .andExpect(status().isOk());
    }
}
