package tourGuide.TP;

import static org.junit.Assert.assertTrue;

import java.util.*;
import java.util.concurrent.*;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import tourGuide.model.external.Attraction;

import tourGuide.model.external.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import tourGuide.helper.InternalTestHelper;
import tourGuide.service.*;
import tourGuide.user.User;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)

public class TestPerformance {

	/*
	 * A note on performance improvements:
	 *     
	 *     The number of users generated for the high volume tests can be easily adjusted via this method:
	 *     
	 *     		InternalTestHelper.setInternalUserNumber(100000);
	 *     
	 *     
	 *     These tests can be modified to suit new solutions, just as long as the performance metrics
	 *     at the end of the tests remains consistent. 
	 * 
	 *     These are performance metrics that we are trying to hit:
	 *     
	 *     highVolumeTrackLocation: 100,000 users within 15 minutes:
	 *     		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
     *
     *     highVolumeGetRewards: 100,000 users within 20 minutes:
	 *          assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	 */
	private Logger logger = LoggerFactory.getLogger(TestPerformance.class);

	@Autowired
	private GpsUtilProxyService gpsUtil;
	@Autowired
	private RewardCentralProxyService rewardCentral;

	@Test
	public void highVolumeTrackLocationNew() {
		Locale.setDefault(Locale.US);
		logger.debug("Start highVolumeTrackLocation");

		RewardsService rewardsService = new RewardsService(gpsUtil, rewardCentral); //FEIGN

		InternalTestHelper.setInternalUserNumber(5000);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService); //FEIGN
		List<User> allUsers = new ArrayList<>();
		allUsers = tourGuideService.getAllUsers();

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.debug("highVolumeTrackLocationNew 1");
		tourGuideService.trackUserLocationList(allUsers);
		logger.debug("highVolumeTrackLocationNew 2");

		stopWatch.stop();
		tourGuideService.tracker.stopTracking();

		System.out.println("***** RESULT ***** RESULT ***** RESULT *****");
		System.out.println("highVolumeTrackLocation: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
		System.out.println("*****   END  *****   END  *****   END  *****");
		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
		logger.debug("End of highVolumeTrackLocation");
	}



	@Test
	public void highVolumeGetRewardsNew() {
		RewardsService rewardsService = new RewardsService(gpsUtil, rewardCentral); //FEIGN
		// Users should be incremented up to 100,000, and test finishes within 20 minutes
		InternalTestHelper.setInternalUserNumber(100);
		//position historique des stopWatch ci dessous
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService); //FEIGN


	    Attraction attraction = gpsUtil.getAttractions().get(0); //FEIGN
		List<User> allUsers = new ArrayList<>();
		allUsers = tourGuideService.getAllUsers();

		allUsers.forEach(u -> u.addToVisitedLocations(new VisitedLocation(u.getUserId(), attraction, new Date()))); // mis dans la boucle

		rewardsService.calculateRewardsList(allUsers);
		for(User user : allUsers) {
			assertTrue(user.getUserRewards().size() > 0);// mis dans la boucle
		}


		stopWatch.stop();
		tourGuideService.tracker.stopTracking();

		System.out.println("***** RESULT ***** RESULT ***** RESULT *****");
		System.out.println("highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
		System.out.println("*****   END  *****   END  *****   END  *****");
		assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));

	}


}
