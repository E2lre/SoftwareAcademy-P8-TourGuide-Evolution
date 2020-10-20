package tourGuide.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tourGuide.model.external.Attraction;
import tourGuide.model.external.Location;
import tourGuide.model.external.VisitedLocation;
import tourGuide.helper.InternalTestHelper;
import tourGuide.helper.Util;
import tourGuide.model.*;
import tourGuide.tracker.Tracker;
import tourGuide.user.User;
import tourGuide.user.UserPreferences;
import tourGuide.user.UserReward;
import tourGuide.model.external.Provider;

@Service
public class TourGuideService {
	private Logger logger = LoggerFactory.getLogger(TourGuideService.class);
	private final GpsUtilProxyService gpsUtilProxyService;

	private final RewardsService rewardsService;
	@Autowired
	private TripPricerProxyService tripPricer;

	public final Tracker tracker;
	boolean testMode = true;
	
	public TourGuideService(GpsUtilProxyService gpsUtil, RewardsService rewardsService) {
		Locale.setDefault(Locale.US);

		this.gpsUtilProxyService = gpsUtil;
		this.rewardsService = rewardsService;

		if(testMode) {
			logger.info("TestMode enabled");
			logger.debug("Initializing users");
			initializeInternalUsers();
			logger.debug("Finished initializing users");
		}
		tracker = new Tracker(this);
		addShutDownHook();
	}

	public List<UserReward> getUserRewards(User user) {
		return user.getUserRewards();
	}
	
	public VisitedLocation getUserLocation(User user) {
		logger.debug("getUserLocation Start");
		VisitedLocation visitedLocation = (user.getVisitedLocations().size() > 0) ?
			user.getLastVisitedLocation() :
			trackUserLocation(user);
		logger.debug("getUserLocation End");
		return visitedLocation;
	}

	/**
	 * get curent location for evryboby (EDE august 2020)
	 * @return list of postion for evrybody
	 */
	public List<UserCurentLocation> getAllCurrentLocations(){
		List<UserCurentLocation> userCurentLocations = new ArrayList<>();
		List<User> userList = getAllUsers();
		for (User user : userList) {

			VisitedLocation lastVisitedLocation = user.getLastVisitedLocation();
			Location location = new Location(lastVisitedLocation.location.latitude,lastVisitedLocation.location.longitude);
			UserCurentLocation userCurentLocation = new UserCurentLocation(user.getUserId(),location);
			userCurentLocations.add(userCurentLocation);
		}

		return userCurentLocations;
	}

	public User getUser(String userName) {
		return internalUserMap.get(userName);
	}
	
	public List<User> getAllUsers() {
		return internalUserMap.values().stream().collect(Collectors.toList());
	}
	
	public void addUser(User user) {
		if(!internalUserMap.containsKey(user.getUserName())) {
			internalUserMap.put(user.getUserName(), user);
		}
	}

	//Ajout EDE 10/2020
	public void setTripPricer(TripPricerProxyService tripPricer) {
		this.tripPricer = tripPricer;
	}
	//EDE August 2020 : modification to check price with preference
	public List<Provider> getTripDeals(User user) {

		int cumulatativeRewardPoints = user.getUserRewards().stream().mapToInt(i -> i.getRewardPoints()).sum();
		List<Provider> providers = tripPricer.getPrice(tripPricerApiKey, user.getUserId(), user.getUserPreferences().getNumberOfAdults(),
				user.getUserPreferences().getNumberOfChildren(), user.getUserPreferences().getTripDuration(), cumulatativeRewardPoints);

		List<Provider> providersResult = new ArrayList<>();
		for (Provider provider : providers) {
			if (provider.price <= user.getUserPreferences().getHighPricePoint().getNumber().doubleValue()) {
				providersResult.add(provider);
			}
		}
		user.setTripDeals(providersResult);
		return providersResult;

	}
	
	public VisitedLocation trackUserLocation(User user) {
		VisitedLocation visitedLocation = gpsUtilProxyService.getUserLocation(user.getUserId());
		user.addToVisitedLocations(visitedLocation);
		rewardsService.calculateRewards(user);
		return visitedLocation;
	}

	/**
	 * track user location for a list of users. If list size is greater than 11, treatment will be asynchron
	 * @param users list of users
	 */
	public void trackUserLocationList(List<User> users) {
		if (users.size() <11) { //Call en synchrone
			logger.debug("trackUserLocationList SYNCHRONE");
			for (User user: users) {
					trackUserLocation(user);
				};
		} else { //Call en asynchrone
			ExecutorService executorService = Executors.newFixedThreadPool(1000);
			logger.debug("trackUserLocationList AAAAAASYNCHRONE");
			for (User user : users) {
				Runnable runnableTask = () -> {
					trackUserLocation(user);
				};
				executorService.execute(runnableTask);
			}
			executorService.shutdown();
			try {
				if (!executorService.awaitTermination(15, TimeUnit.MINUTES)) { //15 minutes est notre objectif
					System.out.println("************************ WARNING - TIME OUT ON trackUserLocationList - WARNING ****************************");
					executorService.shutdownNow();
				}
			} catch (InterruptedException e) {
				System.out.println("************************ WARNING - Exception ON trackUserLocationList - WARNING ***************************");
				System.out.println("message : " + e.getMessage() +" -- localized message : " + e.getLocalizedMessage());
				executorService.shutdownNow();
			}
		}
		return;
	}

//EDE Add for getNearbyAttractions
	/**
	 * Give the 5 nearest attraction for a user (EDE august 2020)
	 * @param user user to check attraction
	 * @param visitedLocation last user's position
	 * @return list of 5 nearest attractions
	 */
	public NearestAttractionsForUser getNearByAttractionsForUSer(User user, VisitedLocation visitedLocation) {
		List<NearestAttraction> nearestAttractions = new ArrayList();
		List<NearestAttraction> nearestAttractionList = new ArrayList();
		List<NearestAttraction> nearestAttractionFinal= new ArrayList();

		Util util = new Util();
		UserDTO userDto = util.convertToDto(user);

		List<Attraction> attractionListLambda = gpsUtilProxyService.getAttractions().parallelStream().collect(Collectors.toList()); //FEIGN
		attractionListLambda.parallelStream().forEach(attractionLb -> {
			Location locationAttraction = new Location(attractionLb.latitude, attractionLb.longitude);
			NearestAttraction nearestAttraction = new NearestAttraction(attractionLb, rewardsService.getDistance(locationAttraction, userDto.getLastVisitedLocation().location), 0);
			nearestAttractions.add(nearestAttraction);
		});

		nearestAttractionList = util.selectFiveProxyAttraction(nearestAttractions);

		nearestAttractionList.parallelStream().forEach(na->{
			int reward =0;
			reward = rewardsService.getRewardPoints(na.getAttraction(), user);
			na.setRewardPoints(reward);
			nearestAttractionFinal.add(na);
		});

		NearestAttractionsForUser nearestAttractionsForUserResult = new NearestAttractionsForUser(userDto,nearestAttractionFinal);
		return nearestAttractionsForUserResult;
	}

	/**
	 * get the user preference for a user (EDE august 2020)
	 * @param userName userName of the user
	 * @return curent preference for the user
	 */
	public UserPreferenceDTO getUserPreference(String userName){

		User curentUser = getUser(userName);
		UserPreferences userPreferenceResult = curentUser.getUserPreferences();
		Util util = new Util();
		return util.convertUserPreferenceToDto(userPreferenceResult);
	}
	/**
	 * set the user preference for a user (EDE august 2020)
	 * @param userName userName of the user
	 * @param userPreference new preference for the user
	 * @return new preference
	 */
	public UserPreferenceDTO setUserPreference(String userName, UserPreferenceDTO userPreference){
		//UserPreferences userPreferenceResult = null;
		User curentUser = getUser(userName);

		//UserPreferences curentUserPreference = curentUser.getUserPreferences();
		Util util = new Util();
		curentUser.setUserPreferences(util.convertDtoToUserPreference(userPreference));
		return util.convertUserPreferenceToDto(curentUser.getUserPreferences());
	}

	private void addShutDownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() { 
		      public void run() {
		        tracker.stopTracking();
		      } 
		    }); 
	}
	
	/**********************************************************************************
	 * 
	 * Methods Below: For Internal Testing
	 * 
	 **********************************************************************************/
	private static final String tripPricerApiKey = "test-server-api-key";
	// Database connection will be used for external users, but for testing purposes internal users are provided and stored in memory
	private final Map<String, User> internalUserMap = new HashMap<>();
	private void initializeInternalUsers() {
		IntStream.range(0, InternalTestHelper.getInternalUserNumber()).forEach(i -> {
			String userName = "internalUser" + i;
			String phone = "000";
			String email = userName + "@tourGuide.com";
			User user = new User(UUID.randomUUID(), userName, phone, email);
			generateUserLocationHistory(user);
			
			internalUserMap.put(userName, user);
		});
		logger.debug("Created " + InternalTestHelper.getInternalUserNumber() + " internal test users.");
	}
	
	private void generateUserLocationHistory(User user) {
		IntStream.range(0, 3).forEach(i-> {
			user.addToVisitedLocations(new VisitedLocation(user.getUserId(), new Location(generateRandomLatitude(), generateRandomLongitude()), getRandomTime()));
		});
	}
	
	private double generateRandomLongitude() {
		double leftLimit = -180;
	    double rightLimit = 180;
	    return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}
	
	private double generateRandomLatitude() {
		double leftLimit = -85.05112878;
	    double rightLimit = 85.05112878;
	    return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}
	
	private Date getRandomTime() {
		LocalDateTime localDateTime = LocalDateTime.now().minusDays(new Random().nextInt(30));
	    return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
	}

}
