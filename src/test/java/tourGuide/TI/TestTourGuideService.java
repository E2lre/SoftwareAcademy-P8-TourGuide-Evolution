package tourGuide.TI;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import tourGuide.model.external.VisitedLocation;
import org.springframework.beans.factory.annotation.Autowired;
import tourGuide.helper.InternalTestHelper;
import tourGuide.model.NearestAttractionsForUser;
import tourGuide.model.UserCurentLocation;
import tourGuide.model.UserPreferenceDTO;
import tourGuide.service.*;
import tourGuide.user.User;
import tourGuide.model.external.Provider;

import static org.junit.Assert.*;

@SpringBootTest
public class TestTourGuideService {
	@Autowired
	private GpsUtilProxyService gpsUtil;
	@Autowired
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
		RewardsService rewardsService = new RewardsService(gpsUtil, rewardCentral);
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);
		
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);
		
		tourGuideService.tracker.stopTracking();
		
		assertEquals(user.getUserId(), visitedLocation.userId);
	}

	/* Modify by EDE */
	@Test
	public void getNearbyAttractions() {
		Locale.setDefault(Locale.US);
		RewardsService rewardsService = new RewardsService(gpsUtil, rewardCentral);
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);
		
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);
		
		NearestAttractionsForUser nearByAttractionsForUSer = tourGuideService.getNearByAttractionsForUSer(user,visitedLocation);
		
		tourGuideService.tracker.stopTracking();
		
		assertEquals(5, nearByAttractionsForUSer.getNearestAttractions().size());
	}

	@Test
	public void getTripDeals() {
		//GIVEN
		Locale.setDefault(Locale.US);
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
		assertNotNull(providers.get(0).toString());
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
