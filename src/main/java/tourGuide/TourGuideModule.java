package tourGuide;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//import gpsUtil.GpsUtil;
import org.springframework.context.annotation.Primary;
import rewardCentral.RewardCentral;
//import tourGuide.proxies.GpsUtilProxy;
import tourGuide.service.GpsUtilProxyService;
import tourGuide.service.GpsUtilProxyServiceImpl;
import tourGuide.service.RewardsService;

@Configuration
public class TourGuideModule {
	/*@Autowired
	private GpsUtilProxyService gpsUtilProxy;*/

	//@Bean
	//public GpsUtil getGpsUtil() {
/*	public GpsUtil getGpsUtil() {
		return new GpsUtil();
	}*/
	@Bean
	@Primary
	public GpsUtilProxyServiceImpl getGpsUtil() {
		return new GpsUtilProxyServiceImpl();
		//return new GpsUtilProxy(); //ICICICICICIC METTRE COUCHE D'ABSTRACTION
	}
	@Bean
	public RewardsService getRewardsService() {
		return new RewardsService(getGpsUtil(), getRewardCentral());
	}
	
	@Bean
	public RewardCentral getRewardCentral() {
		return new RewardCentral();
	}
	
}
