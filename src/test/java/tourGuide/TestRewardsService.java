package tourGuide;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.junit.Ignore;
//import org.junit.Test;
import org.junit.jupiter.api.Test;
//import gpsUtil.GpsUtil;
//import gpsUtil.location.Attraction;
import org.springframework.boot.test.context.SpringBootTest;
import tourGuide.beans.Attraction;
//import gpsUtil.location.VisitedLocation;
import tourGuide.beans.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import rewardCentral.RewardCentral;
import tourGuide.helper.InternalTestHelper;
import tourGuide.proxies.GpsUtilProxy;
import tourGuide.proxies.RewardCentralProxy;
import tourGuide.service.GpsUtilProxyService;
import tourGuide.service.RewardCentralProxyService;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.tracker.Tracker;
import tourGuide.user.User;
import tourGuide.user.UserReward;

@SpringBootTest
public class TestRewardsService {
	private Logger logger = LoggerFactory.getLogger(TestRewardsService.class);
	@Autowired
	//private GpsUtilProxy gpsUtil;
	private GpsUtilProxyService gpsUtil;
	@Autowired
	//private RewardCentralProxy rewardCentral;
	private RewardCentralProxyService rewardCentral;

	@Test
	public void userGetRewards() {
		Locale.setDefault(Locale.US);
		//GpsUtil gpsUtil = new GpsUtil();
		//RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
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
		//GpsUtil gpsUtil = new GpsUtil();
		//RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		RewardsService rewardsService = new RewardsService(gpsUtil, rewardCentral);
		Attraction attraction = gpsUtil.getAttractions().get(0);
		assertTrue(rewardsService.isWithinAttractionProximity(attraction, attraction));
	}
	
	//@Ignore // Needs fixed - can throw ConcurrentModificationException
	//EDE (August 2020) - test correction : correction dans addUserReward pour supprimer la negation
	@Test
	public void nearAllAttractions() {
		//GpsUtil gpsUtil = new GpsUtil();
		//RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		RewardsService rewardsService = new RewardsService(gpsUtil, rewardCentral);
		rewardsService.setProximityBuffer(Integer.MAX_VALUE);

		InternalTestHelper.setInternalUserNumber(1);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);
		
		rewardsService.calculateRewards(tourGuideService.getAllUsers().get(0));
		List<UserReward> userRewards = tourGuideService.getUserRewards(tourGuideService.getAllUsers().get(0));
		tourGuideService.tracker.stopTracking();


		logger.debug( "getAttractions : " + gpsUtil.getAttractions().size());
		logger.debug( "userRewards : " + userRewards.size());
		assertEquals(gpsUtil.getAttractions().size(), userRewards.size());
	}
	
}
