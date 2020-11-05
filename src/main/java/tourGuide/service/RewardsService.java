package tourGuide.service;

import tourGuide.model.external.Attraction;
import tourGuide.model.external.Location;
import tourGuide.model.external.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourGuide.user.User;
import tourGuide.user.UserReward;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.*;


@Service
public class RewardsService  {
    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;
	//ADD EDE
	private ExecutorService executor
			= Executors.newSingleThreadExecutor();

	private Logger logger = LoggerFactory.getLogger(RewardsService.class);
	// proximity in miles
    private int defaultProximityBuffer = 10;
	private int proximityBuffer = defaultProximityBuffer;
	private int attractionProximityRange = 20000; //TODO EDE a enlever

	@Autowired
	private GpsUtilProxyService gpsUtil;

	@Autowired
	private RewardCentralProxyService rewardsCentral;

	public RewardsService(GpsUtilProxyService gpsUtil, RewardCentralProxyService rewardCentral) {
		this.gpsUtil = gpsUtil;
		this.rewardsCentral = rewardCentral;
	}

	public RewardsService() {

	}
	
	public void setProximityBuffer(int proximityBuffer) {
		this.proximityBuffer = proximityBuffer;
	}
	
	public void setDefaultProximityBuffer() {
		proximityBuffer = defaultProximityBuffer;
	}

	/**
	 * Calculate reward for a list of users. If list size is greater than 11, treatment will be asynchron
	 * @param users list of users
	 */
	public  void calculateRewardsList(List<User> users) {
		Locale.setDefault(Locale.US);
		if (users.size() <11) { //Call en synchrone
			logger.debug("calculateRewardsList SYNCHRONE");
			for (User user : users) {
					calculateRewards_standard(user);
				};
		} else {
			logger.debug("calculateRewardsList AAAAAASYNCHRONE");
			ExecutorService executorServiceLocal = Executors.newFixedThreadPool(50);
			for (User user : users) {
				Runnable runnableTask = () -> {
					calculateRewards_standard(user);
				};

				executorServiceLocal.execute(runnableTask);
			}
			executorServiceLocal.shutdown();

			try {
				
				if (!executorServiceLocal.awaitTermination(20, TimeUnit.MINUTES)) { //20 minutes est notre objectif
					
					System.out.println("************************ WARNING - TIME OUT ON calculateRewardsList - WARNING ****************************");
					executorServiceLocal.shutdownNow();
				}
			} catch (InterruptedException e) {
				System.out.println("************************ WARNING - Exception ON calculateRewardsList - WARNING ***************************");
				System.out.println("localized message : " + e.getLocalizedMessage());
				executorServiceLocal.shutdownNow();
			}
		}

	}
	public  void calculateRewards(User user) {
		calculateRewards_standard(user);
	}

	public  void calculateRewards_standard(User user) {
	//E2LRE August 2020 :Correction to avoid ConcurrentModificationException in TestPerform

		CopyOnWriteArrayList<VisitedLocation> userLocations = new CopyOnWriteArrayList<>();
		userLocations.addAll(user.getVisitedLocations());

		CopyOnWriteArrayList<Attraction> attractions = new CopyOnWriteArrayList<>();

		logger.debug("calculateRewards_standard call proxy");
		attractions.addAll(gpsUtil.getAttractions());
		logger.debug("calculateRewards_standard call proxy End");

			userLocations.stream().forEach(visitedLocation -> {
				attractions.stream().forEach(attraction -> {
				if (user.getUserRewards().stream().filter(r -> r.attraction.attractionName.equals(attraction.attractionName)).count() == 0) {
					if (nearAttraction(visitedLocation, attraction)) {
						logger.debug("calculate rewards start");
							user.addUserReward(new UserReward(visitedLocation, attraction, getRewardPoints(attraction, user)));
					}
				}
			});
		});

	}
	public boolean isWithinAttractionProximity(Attraction attraction, Location location) {
		return getDistance(attraction, location) > attractionProximityRange ? false : true;
	}
	
	private boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
		return getDistance(attraction, visitedLocation.location) > proximityBuffer ? false : true;
	}

	public int getRewardPoints(Attraction attraction, User user) {
		return rewardsCentral.getAttractionRewardPoints(attraction.attractionId.toString(), user.getUserId().toString());
	}

	public double getDistance(Location loc1, Location loc2) {
        double lat1 = Math.toRadians(loc1.latitude);
        double lon1 = Math.toRadians(loc1.longitude);
        double lat2 = Math.toRadians(loc2.latitude);
        double lon2 = Math.toRadians(loc2.longitude);

        double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
                               + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

        double nauticalMiles = 60 * Math.toDegrees(angle);
        double statuteMiles = STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
        return statuteMiles;
	}
}
