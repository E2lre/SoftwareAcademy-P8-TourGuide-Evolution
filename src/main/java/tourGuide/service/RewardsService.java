package tourGuide.service;

//import gpsUtil.location.Attraction;
import tourGuide.beans.Attraction;
//import gpsUtil.location.Location;
import tourGuide.beans.Location;
//import gpsUtil.location.VisitedLocation;
import tourGuide.beans.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import rewardCentral.RewardCentral;
//import tourGuide.proxies.GpsUtilProxy;
import tourGuide.proxies.GpsUtilProxy;
import tourGuide.proxies.RewardCentralProxy;
import tourGuide.user.User;
import tourGuide.user.UserReward;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;


@Service
public class RewardsService  {
//	public class RewardsService extends Thread {
    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;
	public CompletableFuture<Void> completableFuture;
	ExecutorService executorService = Executors.newFixedThreadPool(1000);

	static Semaphore semaphore = new Semaphore(1);

	//ADD EDE
	private ExecutorService executor
			= Executors.newSingleThreadExecutor();

	private Logger logger = LoggerFactory.getLogger(RewardsService.class);
	// proximity in miles
    private int defaultProximityBuffer = 10;
	private int proximityBuffer = defaultProximityBuffer;
	private int attractionProximityRange = 20000; //TODO EDE a enlever
	//private final GpsUtil gpsUtil;

	//@Qualifier("gpsUtilProxyServiceImpl")
	@Autowired
	private GpsUtilProxyService gpsUtil;
	//private final GpsUtilProxy gpsUtil;
	//private  GpsUtilProxy gpsUtil;

	//private final RewardCentral rewardsCentral;
	@Autowired
	private RewardCentralProxyService rewardsCentral;
	//private final RewardCentralProxy rewardsCentral;
	//private  RewardCentralProxy rewardsCentral;
	
	//public RewardsService(GpsUtil gpsUtil, RewardCentral rewardCentral) {
	//public RewardsService(@Qualifier("gpsUtilProxyServiceImpl") GpsUtilProxyService gpsUtil, RewardCentral rewardCentral) { //FEIGN
	//public RewardsService(GpsUtilProxyService gpsUtil, RewardCentral rewardCentral) { //FEIGN
	//public RewardsService(GpsUtilProxy gpsUtil, RewardCentralProxy rewardCentral) { //FEIGN
	public RewardsService(GpsUtilProxyService gpsUtil, RewardCentralProxyService rewardCentral) { //FEIGN
		this.gpsUtil = gpsUtil;
		this.rewardsCentral = rewardCentral;
	}

	//Ajout EDE
	public RewardsService() { //FEIGN

	}
	
	public void setProximityBuffer(int proximityBuffer) {
		this.proximityBuffer = proximityBuffer;
	}
	
	public void setDefaultProximityBuffer() {
		proximityBuffer = defaultProximityBuffer;
	}


	public  void calculateRewardsList(List<User> users) {
		Locale.setDefault(Locale.US);
		//logger.debug("calculateRewardsList Start");
		if (users.size() <11) { //Call en synchrone
			logger.debug("calculateRewardsList SYNCHRONE");
			for (User user : users) {
					calculateRewards_standard(user);
				};
		} else {
			logger.debug("calculateRewardsList AAAAAASYNCHRONE");
			ExecutorService executorServiceLocal = Executors.newFixedThreadPool(1000);
			for (User user : users) {
				Runnable runnableTask = () -> {
					//logger.debug("run-------------------------- Start");
					calculateRewards_standard(user);
					//logger.debug("run-------------------------- End");
				};
				//logger.debug("execute");
				executorServiceLocal.execute(runnableTask);
			}

			//logger.debug("shutdown");
			executorServiceLocal.shutdown();
			//logger.debug("calculateRewardsList waiting");
			try {
				//if (!executor.awaitTermination(800, TimeUnit.MILLISECONDS)) {
				if (!executorServiceLocal.awaitTermination(20, TimeUnit.MINUTES)) { //15 minutes est notre objectif
					System.out.println("**********************************************************************************************************");
					System.out.println("************************ WARNING - TIME OUT ON calculateRewardsList - WARNING ****************************");
					System.out.println("**********************************************************************************************************");
					executorServiceLocal.shutdownNow();
				}
			} catch (InterruptedException e) {
				System.out.println("**********************************************************************************************************");
				System.out.println("************************ WARNING - Exception ON calculateRewardsList - WARNING ***************************");
				System.out.println("message : " + e.getMessage());
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
/*		 List<VisitedLocation> userLocations = user.getVisitedLocations();
		 List<Attraction> attractions = gpsUtil.getAttractions();
*/
		//logger.debug("calculateRewards_standard start");
		CopyOnWriteArrayList<VisitedLocation> userLocations = new CopyOnWriteArrayList<>();
		userLocations.addAll(user.getVisitedLocations());

		CopyOnWriteArrayList<Attraction> attractions = new CopyOnWriteArrayList<>();
		//attractions.addAll(gpsUtil.getAttractions());
		logger.debug("calculateRewards_standard call proxy");
		attractions.addAll(gpsUtil.getAttractions()); //feign icici
		logger.debug("calculateRewards_standard call proxy End");

		/****** Mise en place de Executor Services ****************/
		//ExecutorService executor = Executors.newFixedThreadPool(1000);


		userLocations.stream().forEach(visitedLocation -> {
				attractions.stream().forEach(attraction -> {
				if (user.getUserRewards().stream().filter(r -> r.attraction.attractionName.equals(attraction.attractionName)).count() == 0) { //TODO voir efficacité du parallel ==>0
					if (nearAttraction(visitedLocation, attraction)) {
							user.addUserReward(new UserReward(visitedLocation, attraction, getRewardPoints(attraction, user)));
					}
				}
			});
		});
		//logger.debug("End of calculateRewards");
		//logger.debug("calculateRewards_standard End");
	}
	public  void calculateRewards_async(User user) {
//E2LRE August 2020 :Correction to avoid ConcurrentModificationException in TestPerform
/*		 List<VisitedLocation> userLocations = user.getVisitedLocations();
		 List<Attraction> attractions = gpsUtil.getAttractions();
*/
		CopyOnWriteArrayList<VisitedLocation> userLocations = new CopyOnWriteArrayList<>();
		userLocations.addAll(user.getVisitedLocations());

		CopyOnWriteArrayList<Attraction> attractions = new CopyOnWriteArrayList<>();
		attractions.addAll(gpsUtil.getAttractions());

		/****** Mise en place de Executor Services ****************/
		//ExecutorService executor = Executors.newFixedThreadPool(1000);

		//userLocations.parallelStream().forEach(visitedLocation -> rewardsService.calculateRewards(u));
		//for (VisitedLocation visitedLocation : userLocations) {
		//userLocations.parallelStream().forEach(visitedLocation -> {
		userLocations.stream().forEach(visitedLocation -> {
			//for (Attraction attraction : attractions) {
			//attractions.parallelStream().forEach(attraction -> {
			attractions.stream().forEach(attraction -> {
				if (user.getUserRewards().stream().filter(r -> r.attraction.attractionName.equals(attraction.attractionName)).count() == 0) { //TODO voir efficacité du parallel ==>0
					//if (user.getUserRewards().parallelStream().filter(r -> r.attraction.attractionName.equals(attraction.attractionName)).count() == 0) {
					if (nearAttraction(visitedLocation, attraction)) {
						//Ajout de l'async

//						user.addUserReward(new UserReward(visitedLocation, attraction, getRewardPoints(attraction, user)));
								Runnable runnableTask = () -> {
									logger.debug("run- calculateRewards_async------------------------- start"+ user.getUserName());
									user.addUserReward(new UserReward(visitedLocation, attraction, getRewardPoints(attraction, user)));
									logger.debug("run- calculateRewards_async------------------------- end"+ user.getUserName());
								};
						//logger.debug("exec ");
								executorService.execute(runnableTask);

					}
				}
			});
		});

			logger.debug("calculateRewards_async shutdown");
			executorService.shutdown();

			try {
				//if (!executor.awaitTermination(800, TimeUnit.MILLISECONDS)) {
				if (!executorService.awaitTermination(1, TimeUnit.MINUTES)) { //15 minutes est notre objectif
					logger.debug("************* calculateRewards_async end now REward********************");
					executorService.shutdownNow();
				}
			} catch (InterruptedException e) {
				logger.debug("************* calculateRewards_async end now catch REward *************");
				executorService.shutdownNow();
			}
			logger.debug(" calculateRewards_async end");
		/****** fin de Mise en place de Executor Services ****************/
		//logger.debug("End of calculateRewards");
	}
	public boolean isWithinAttractionProximity(Attraction attraction, Location location) {
		return getDistance(attraction, location) > attractionProximityRange ? false : true;
	}
	
	private boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
		return getDistance(attraction, visitedLocation.location) > proximityBuffer ? false : true;
	}

	public int getRewardPoints(Attraction attraction, User user) {
		return rewardsCentral.getAttractionRewardPoints(attraction.attractionId.toString(), user.getUserId().toString());
	//	return getRewardPoints_Async(attraction, user);

	}
	public int getRewardPoints_exec(Attraction attraction, User user) {
		return rewardsCentral.getAttractionRewardPoints(attraction.attractionId.toString(), user.getUserId().toString());
	}

	public int getRewardPoints_Async(Attraction attraction, User user) {
		logger.debug("getRewardPoints_Async start Ascyn");
		String result ="";
		int res = 0;
/*		CompletableFuture<Void> completableFuture = new CompletableFuture<>();
		completableFuture.supplyAsync(() -> {
			logger.debug("call exec");
			getRewardPoints_exec( attraction, user);
		});*/
		//CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(new Supplier<String>() {
/*
		 completableFuture = CompletableFuture.supplyAsync(new Supplier<String>() {
			@Override
			public String get() {
				String result ="0";
				int res = 0;
				res = getRewardPoints_exec( attraction, user);
				result.valueOf(res);
				return result;
			}
		});

		try {
			logger.debug("getRewardPoints_Async get start");
			result = completableFuture.get();
			logger.debug("getRewardPoints_Async get end");
		} catch (InterruptedException e) {
			logger.debug("************* end now catch REward *************" + e.getMessage());
			//ExecutionException, InterruptedException
		} catch (ExecutionException e) {
			logger.debug("************* end now catch REward *************" + e.getMessage());
			e.printStackTrace();
		}
		res = Integer.parseInt(result);
		logger.debug("getRewardPoints_Async get Finish");
*/

		return res;
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
