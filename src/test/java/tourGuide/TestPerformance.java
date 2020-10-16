package tourGuide;

import static org.junit.Assert.assertTrue;

import java.util.*;
import java.util.concurrent.*;

import gpsUtil.GpsUtil;
import org.apache.commons.lang3.time.StopWatch;
//import org.junit.Ignore;
//import org.junit.Test;
import org.junit.jupiter.api.Test;

//import gpsUtil.GpsUtil;
//import gpsUtil.location.Attraction;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
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
import tourGuide.service.*;
import tourGuide.user.User;
import tourGuide.user.UserReward;

@SpringBootTest
//@ContextConfiguration(classes = {PerfConfig.class})
//@ContextConfiguration(classes = {TourGuideModule.class})
//@EnableAutoConfiguration
//@EnableFeignClients("tourGuide")
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
/*	static Semaphore semaphore = new Semaphore(1);
	private int cpt;*/
	//@Ignore
	//@Qualifier("gpsUtilProxyServiceImpl")
	/*@Autowired
	private GpsUtilProxyService gpsUtilProxy;
*/

	@Autowired
	private GpsUtilProxyService gpsUtil;
	//private GpsUtilProxy gpsUtil;
	@Autowired
	//private RewardCentralProxy rewardCentral;
	private RewardCentralProxyService rewardCentral;
	//@Autowired
	//private RewardsService rewardsService;
/*	@Autowired
	private GpsUtilProxy psUtilProxy;*/

	//@Test
	@Test
	public void highVolumeTrackLocationNew() {
		Locale.setDefault(Locale.US);
		logger.debug("Start highVolumeTrackLocation");

		//List<Attraction> attractionList = gpsUtil.getAttractions(); //Test à supprimer
		/*gpsUtil = new GpsUtilProxyServiceImpl();
		rewardCentral = new RewardCentralProxyServiceImpl();*/

/*
		GpsUtil gpsUtil = new GpsUtil();
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
*/

		//GpsUtilProxyServiceImpl gpsUtilProxy = new GpsUtilProxyServiceImpl();

		//gpsUtil = new GpsUtilProxyServiceImpl();


		//RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral()); //FEIGN
		RewardsService rewardsService = new RewardsService(gpsUtil, rewardCentral); //FEIGN

		// Users should be incremented up to 100,000, and test finishes within 15 minutes
		InternalTestHelper.setInternalUserNumber(1000);
		//TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService); //FEIGN
		List<User> allUsers = new ArrayList<>();
		allUsers = tourGuideService.getAllUsers();

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		logger.debug("highVolumeTrackLocationNew 1");
		tourGuideService.trackUserLocationList(allUsers);
		/*for(User user : allUsers) {
			tourGuideService.trackUserLocation(user);
		}*/
		//allUsers.parallelStream().forEach(u -> tourGuideService.trackUserLocation(u));
		logger.debug("highVolumeTrackLocationNew 2");

		stopWatch.stop();
		tourGuideService.tracker.stopTracking();

		System.out.println("***** RESULT ***** RESULT ***** RESULT *****");
		System.out.println("highVolumeTrackLocation: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
		System.out.println("*****   END  *****   END  *****   END  *****");
		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
		logger.debug("End of highVolumeTrackLocation");
	}


	//@Ignore
	//@Test
	@Test
	public void highVolumeGetRewardsNew() {
		//Locale.setDefault(Locale.US);
		logger.debug("Start highVolumeGetRewardsNew");

		//GpsUtil gpsUtil = new GpsUtil();
		//RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		//GpsUtilProxyServiceImpl gpsUtilProxy = new GpsUtilProxyServiceImpl();
		//RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral()); //FEIGN
		RewardsService rewardsService = new RewardsService(gpsUtil, rewardCentral); //FEIGN
		// Users should be incremented up to 100,000, and test finishes within 20 minutes
		InternalTestHelper.setInternalUserNumber(1000);
//position historique des stopWatch ci dessous
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		//TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService); //FEIGN


	    //Attraction attraction = gpsUtil.getAttractions().get(0);
		Attraction attraction = gpsUtil.getAttractions().get(0); //FEIGN
		List<User> allUsers = new ArrayList<>();
		allUsers = tourGuideService.getAllUsers();

		logger.debug("highVolumeGetRewardsnew 1");
		allUsers.forEach(u -> u.addToVisitedLocations(new VisitedLocation(u.getUserId(), attraction, new Date()))); // mis dans la boucle
		//allUsers.parallelStream().forEach(u -> u.addToVisitedLocations(new VisitedLocation(u.getUserId(), attraction, new Date())));

		logger.debug("highVolumeGetRewardsnew 2");
		rewardsService.calculateRewardsList(allUsers);
			//allUsers.forEach(u -> rewardsService.calculateRewards(u));
			//allUsers.parallelStream().forEach(u -> rewardsService.calculateRewards(u));
		logger.debug("highVolumeGetRewardsnew 3");
		for(User user : allUsers) {
			assertTrue(user.getUserRewards().size() > 0);// mis dans la boucle
		}
		logger.debug("highVolumeGetRewardsnew 4");


		stopWatch.stop();
		tourGuideService.tracker.stopTracking();

		System.out.println("***** RESULT ***** RESULT ***** RESULT *****");
		System.out.println("highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
		System.out.println("*****   END  *****   END  *****   END  *****");
		assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
		logger.debug("End of highVolumeGetRewards");
	}


}
