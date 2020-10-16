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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tourGuide.model.UserPreferenceDTO;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TestTourGuideControlerTU {
    @Autowired
    private MockMvc mockMvc;
  //  private MockMvc mockMvc;
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
        //setUpEach();
        //GIVEN : Give a person to get
        //mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        Mockito.when(tourGuideService.getUser(anyString())).thenReturn(user);
        Mockito.when(tourGuideService.getUserPreference(anyString())).thenReturn(userPreferenceDTO);
        //WHEN //THEN return the user préference
        mockMvc.perform(get("/getUserPreference?userName="+user))
                .andExpect(status().isOk());

  //TODO : voir si pour le controleur, ce ne serait pas un test d'integ
    }

}
