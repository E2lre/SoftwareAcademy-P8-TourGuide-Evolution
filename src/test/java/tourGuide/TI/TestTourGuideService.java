package tourGuide.TI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.junit.Ignore;
//import org.junit.Test;
import org.junit.jupiter.api.Test;
//import gpsUtil.GpsUtil;
//import gpsUtil.location.Attraction;
//import gpsUtil.location.VisitedLocation;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import tourGuide.beans.VisitedLocation;
import org.springframework.beans.factory.annotation.Autowired;
import rewardCentral.RewardCentral;
import tourGuide.helper.InternalTestHelper;
import tourGuide.model.NearestAttraction;
import tourGuide.model.NearestAttractionsForUser;
import tourGuide.model.UserCurentLocation;
import tourGuide.model.UserPreferenceDTO;
import tourGuide.proxies.GpsUtilProxy;
import tourGuide.proxies.RewardCentralProxy;
import tourGuide.service.*;
import tourGuide.user.User;
//import tripPricer.Provider;
import tourGuide.model.external.Provider;

@SpringBootTest
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class TestTourGuideService {
	@Autowired
	//private GpsUtilProxy gpsUtil;
	private GpsUtilProxyService gpsUtil;
	@Autowired
	//private RewardCentralProxy rewardCentral;
	private RewardCentralProxyService rewardCentral;

	@Autowired
	private TripPricerProxyService tripPricerProxyService;
	@Test
	public void getUserLocation() {
		Locale.setDefault(Locale.US);
		RewardsService rewardsService = new RewardsService(gpsUtil, rewardCentral);
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);
		
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);
		tourGuideService.tracker.stopTracking();
		assertTrue(visitedLocation.userId.equals(user.getUserId()));
	}
   /* Added by EDE */
	@Test
	public void getAllCurrentLocations (){
		Locale.setDefault(Locale.US);
		//GpsUtil gpsUtil = new GpsUtil();
		//RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		RewardsService rewardsService = new RewardsService(gpsUtil, rewardCentral);
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		tourGuideService.addUser(user);
		VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);


		List<UserCurentLocation> UserCurentLocation = tourGuideService.getAllCurrentLocations();

		assertEquals(1,UserCurentLocation.size());


	}

	@Test
	public void addUser() {
		//GpsUtil gpsUtil = new GpsUtil();
		//RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		RewardsService rewardsService = new RewardsService(gpsUtil, rewardCentral);
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);
		
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

		tourGuideService.addUser(user);
		tourGuideService.addUser(user2);
		
		User retrivedUser = tourGuideService.getUser(user.getUserName());
		User retrivedUser2 = tourGuideService.getUser(user2.getUserName());

		tourGuideService.tracker.stopTracking();
		
		assertEquals(user, retrivedUser);
		assertEquals(user2, retrivedUser2);
	}
	
	@Test
	public void getAllUsers() {
		//GpsUtil gpsUtil = new GpsUtil();
		//RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		RewardsService rewardsService = new RewardsService(gpsUtil, rewardCentral);
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);
		
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

		tourGuideService.addUser(user);
		tourGuideService.addUser(user2);
		
		List<User> allUsers = tourGuideService.getAllUsers();

		tourGuideService.tracker.stopTracking();
		
		assertTrue(allUsers.contains(user));
		assertTrue(allUsers.contains(user2));
	}
	
	@Test
	public void trackUser() {
		Locale.setDefault(Locale.US);
		//GpsUtil gpsUtil = new GpsUtil();
		//RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		RewardsService rewardsService = new RewardsService(gpsUtil, rewardCentral);
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);
		
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);
		
		tourGuideService.tracker.stopTracking();
		
		assertEquals(user.getUserId(), visitedLocation.userId);
	}

	/* Modify by EDE */
	//@Ignore // Not yet implemented
	@Test
	public void getNearbyAttractions() {
		Locale.setDefault(Locale.US);
		//GpsUtil gpsUtil = new GpsUtil();
		RewardsService rewardsService = new RewardsService(gpsUtil, rewardCentral);
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);
		
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);
		
		//List<Attraction> attractions = tourGuideService.getNearByAttractions(visitedLocation);
		NearestAttractionsForUser nearByAttractionsForUSer = tourGuideService.getNearByAttractionsForUSer(user,visitedLocation);
		
		tourGuideService.tracker.stopTracking();
		
		//assertEquals(5, attractions.size());
		assertEquals(5, nearByAttractionsForUSer.getNearestAttractions().size());
	}

	@Test
	public void getTripDeals() {
		//GIVEN
		Locale.setDefault(Locale.US);
		//GpsUtil gpsUtil = new GpsUtil();
		//RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		RewardsService rewardsService = new RewardsService(gpsUtil, rewardCentral);
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);
		tourGuideService.setTripPricer(tripPricerProxyService);
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");

		//WHEN
		List<Provider> providers = tourGuideService.getTripDeals(user);
		
		tourGuideService.tracker.stopTracking();
		
		//THEN
		assertEquals(5, providers.size()); //getprice ne retourne que 5 providers

	}

	@Test
	public void getUserPreference() {
		//GIVEN
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");

		RewardsService rewardsService = new RewardsService(gpsUtil, rewardCentral);
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);
		tourGuideService.addUser(user);

		//WHEN
		UserPreferenceDTO userPreferenceDTO = tourGuideService.getUserPreference(user.getUserName());
		//THEN
		assertEquals(userPreferenceDTO.getCurrency(),"USD");


	}
}
