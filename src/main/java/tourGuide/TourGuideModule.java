package tourGuide;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//import gpsUtil.GpsUtil;
import org.springframework.context.annotation.Primary;
import rewardCentral.RewardCentral;
//import tourGuide.proxies.GpsUtilProxy;
import tourGuide.proxies.GpsUtilProxy;
import tourGuide.service.*;

@Configuration
public class TourGuideModule {
/*


	@Bean
	//@Primary
	public GpsUtilProxyService getGpsUtilProxy() {
		return new GpsUtilProxyServiceImpl();
	}
	@Bean
	//@Primary
	public RewardCentralProxyService getRewardCentralProxy() {
		return new RewardCentralProxyServiceImpl();
	}
	@Bean
	public RewardsService getRewardsService() {
		//return new RewardsService(getGpsUtil(), getRewardCentral());
		//return new RewardsService(getGpsUtil, getRewardCentral());
		return new RewardsService(getGpsUtilProxy(), getRewardCentralProxy());
	}
	
	@Bean
	public RewardCentral getRewardCentral() {
		return new RewardCentral();
	}
*/

}
