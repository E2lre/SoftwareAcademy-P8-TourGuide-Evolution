package tourGuide;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ContextConfiguration;
import rewardCentral.RewardCentral;
import tourGuide.proxies.GpsUtilProxy;
import tourGuide.proxies.RewardCentralProxy;
import tourGuide.service.*;

//@Configuration
//@EnableFeignClients
public class PerfConfig {
/*
    @TestConfiguration
    static class GpsTestsContextConfiguration {
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


        @Bean
        public TourGuideService getTourGuideService() {
            return new TourGuideService(getGpsUtilProxy(), getRewardsService());
        }
    }
*/

}
