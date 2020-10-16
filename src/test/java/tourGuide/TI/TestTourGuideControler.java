package tourGuide.TI;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RequestParam;
import tourGuide.model.UserPreferenceDTO;
import tourGuide.user.User;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TestTourGuideControler {
    @Autowired
    private MockMvc mockMvc;
    private User user;
    private UserPreferenceDTO userPreferenceDTO;
    private String userName;
    private String badUserName;

    @BeforeEach
    private void setUpEach() {
        user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        userPreferenceDTO = new UserPreferenceDTO(1, "US", 0.0d, 1000.0d, 5, 2, 1, 1);
        userName = "internalUser2";
        badUserName = "internalUser99999999";
    }

    /*------------------------------ Get ------------------------------*/
    @Test
    public void getUserPreference_existingUserName_UserPreferenceIsDone() throws Exception {
        //GIVEN : for a existing user
        //WHEN call Controller
        //THEN return is OK
        userName = "internalUser3";
        mockMvc.perform(get("/getUserPreference?userName=" + userName))
                .andExpect(status().isOk());
    }

    @Test
    public void getUserPreference_inexistingUserName_errorIsReturn() throws Exception {
        //GIVEN : for a existing user
        //WHEN call Controller
        //THEN return is OK
        userName = "internalUser4";
        mockMvc.perform(get("/getUserPreference?userName=" + badUserName))
                .andExpect(status().isNotFound());
    }
    /*------------------------------ Get ------------------------------*/
    @Test
    public void getTripDeals_existingUserName_aTripIsDone() throws Exception {
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
 public void getLocation_existingUserName_aLoacationIsDone() throws Exception {
     mockMvc.perform(get("/getLocation?userName=" + userName))
             .andExpect(status().isOk());
 }
    /*------------------------------ Get ------------------------------*/
    @Test
    public void getRewards_existingUserName_RewardIsDone() throws Exception {
        mockMvc.perform(get("/getRewards?userName=" + userName))
                .andExpect(status().isOk());
    }
}
