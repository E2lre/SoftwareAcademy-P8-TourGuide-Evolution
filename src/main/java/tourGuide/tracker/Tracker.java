package tourGuide.tracker;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tourGuide.service.TourGuideService;
import tourGuide.user.User;

public class Tracker extends Thread {
	private Logger logger = LoggerFactory.getLogger(Tracker.class);
	private static final long trackingPollingInterval = TimeUnit.MINUTES.toSeconds(5);
	private final ExecutorService executorService = Executors.newSingleThreadExecutor();
	private final TourGuideService tourGuideService;
	private boolean stop = false;

	public Tracker(TourGuideService tourGuideService) {
		this.tourGuideService = tourGuideService;
		executorService.submit(this);


		logger.debug("tracker submit");

	}
	
	/**
	 * Assures to shut down the Tracker thread
	 */
	public void stopTracking() {
		stop = true;
		executorService.shutdownNow();
	}
	
/*	@Override
	public void run() {
		StopWatch stopWatch = new StopWatch();
		while(true) {
			if(Thread.currentThread().isInterrupted() || stop) {
				logger.debug("Tracker stopping");
				break;
			}

			List<User> users = tourGuideService.getAllUsers();
			logger.debug("Begin Tracker. Tracking " + users.size() + " users.");
			stopWatch.start();
			logger.debug("start loop");
			users.forEach(u -> tourGuideService.trackUserLocation(u));
			logger.debug("End loop");
			stopWatch.stop();
			logger.debug("Tracker Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
			stopWatch.reset();
			try {
				logger.debug("Tracker sleeping");
				TimeUnit.SECONDS.sleep(trackingPollingInterval);
				logger.debug("Tracker END of sleeping"); //TODO EDE a retirer
			} catch (InterruptedException e) {
				break;
			}
		}

	}*/
	//EDE SPtembre 2020 : call de trackUserLocationList pour gérer l'async
	@Override
	public void run() {
		StopWatch stopWatch = new StopWatch();
		while(true) {
			if(Thread.currentThread().isInterrupted() || stop) {
				logger.debug("Tracker stopping");
				break;
			}

			List<User> users = tourGuideService.getAllUsers();
			logger.debug("Begin Tracker. Tracking " + users.size() + " users.");
			stopWatch.start();
			logger.debug("start loop");
			tourGuideService.trackUserLocationList(users);
			//users.forEach(u -> tourGuideService.trackUserLocation(u));
			logger.debug("End loop");
			stopWatch.stop();
			logger.debug("Tracker Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
			stopWatch.reset();
			try {
				logger.debug("Tracker sleeping");
				TimeUnit.SECONDS.sleep(trackingPollingInterval);
				logger.debug("Tracker END of sleeping"); //TODO EDE a retirer
			} catch (InterruptedException e) {
				break;
			}
		}

	}
	/* EDE Septmbre 2020 evolution du tracker pour lance les trackUserLocation en asynchrone */
	/*@Override
	public void run() {
		StopWatch stopWatch = new StopWatch();
		while(true) {
			if(Thread.currentThread().isInterrupted() || stop) {
				logger.debug("Tracker stopping");
				break;
			}

			List<User> users = tourGuideService.getAllUsers();
			logger.debug("Begin Tracker. Tracking " + users.size() + " users.");
			stopWatch.start();
			logger.debug("start exec");
			ExecutorService executor = Executors.newFixedThreadPool(1000);
			users.forEach(u -> {
				logger.debug("loop");
				Runnable runnableTask = () -> {
					logger.debug("run- Start-------------------------"+ u.getUserName());
					tourGuideService.trackUserLocation(u);
					logger.debug("run- END -------------------------"+ u.getUserName());
			};
				executor.execute(runnableTask);
			});
			logger.debug("shutdown");
			executor.shutdown();

			try {
				if (!executor.awaitTermination(15, TimeUnit.MINUTES)) {
					logger.debug("************* end now ********************");
					executor.shutdownNow();
				}
			} catch (InterruptedException e) {
				logger.debug("************* end now catch *************");
				executor.shutdownNow();
			}
			logger.debug("end");

			stopWatch.stop();
			logger.debug("Tracker Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
			stopWatch.reset();
			try {
				logger.debug("Tracker sleeping");
				TimeUnit.SECONDS.sleep(trackingPollingInterval);
				logger.debug("Tracker END of sleeping"); //TODO EDE a retirer
			} catch (InterruptedException e) {
				break;
			}
		}

	}*/
}
