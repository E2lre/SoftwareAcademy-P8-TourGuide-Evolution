package tourGuide.TI;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import tourGuide.model.external.Attraction;

import tourGuide.model.external.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import tourGuide.helper.InternalTestHelper;
import tourGuide.service.GpsUtilProxyService;
import tourGuide.service.RewardCentralProxyService;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;
import tourGuide.user.UserReward;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class TestRewardsService {
	private Logger logger = LoggerFactory.getLogger(TestRewardsService.class);
	@Autowired
	private GpsUtilProxyService gpsUtil;
	@Autowired
	private RewardCentralProxyService rewardCentral;

	@Test
	public void userGetRewards() {
		Locale.setDefault(Locale.US);
		RewardsService rewardsService = new RewardsService(gpsUtil, rewardCentral);

		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);
		
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		Attraction attraction = gpsUtil.getAttractions().get(0);
		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));
		tourGuideService.trackUserLocation(user);
		List<UserReward> userRewards = user.getUserRewards();
		tourGuideService.tracker.stopTracking();
		assertTrue(userRewards.size() == 1);
	}
	
	@Test
	public void isWithinAttractionProximity() {
		RewardsService rewardsService = new RewardsService(gpsUtil, rewardCentral);
		Attraction attraction = gpsUtil.getAttractions().get(0);
		assertTrue(rewardsService.isWithinAttractionProximity(attraction, attraction));
	}
	
	//EDE (August 2020) - test correction : correction dans addUserReward pour supprimer la negation
	@Test
	public void nearAllAttractions() {
		RewardsService rewardsService = new RewardsService(gpsUtil, rewardCentral);
		rewardsService.setProximityBuffer(Integer.MAX_VALUE);

		InternalTestHelper.setInternalUserNumber(1);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);
		tourGuideService.tracker.stopTracking(); //Deplacement ede

		User myUSer = tourGuideService.getAllUsers().get(0);
		rewardsService.calculateRewards(tourGuideService.getAllUsers().get(0));
		List<UserReward> userRewards = tourGuideService.getUserRewards(tourGuideService.getAllUsers().get(0));
		//tourGuideService.tracker.stopTracking();

		int gpsSize = gpsUtil.getAttractions().size();
		int userRewardSize =   userRewards.size();
		logger.debug( "getAttractions : " + gpsUtil.getAttractions().size());
		logger.debug( "userRewards : " + userRewards.size());

		assertEquals(gpsUtil.getAttractions().size(), userRewards.size());

	}
}
