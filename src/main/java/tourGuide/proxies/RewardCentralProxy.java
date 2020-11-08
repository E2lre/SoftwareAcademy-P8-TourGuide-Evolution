package tourGuide.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * FeignClient interface for RewardCentral proxy on port 8083
 */
@FeignClient(name = "rewardCentral", url="localhost:8083")
public interface RewardCentralProxy {

    /**
     * call RewardCentral proxy for attraction reward point
     * @param attractionId uudi Attraction Id visited
     * @param userId uuid fot user Id
     * @return reward points for the attraction
     */
    @GetMapping(value ="/getAttractionRewardPoints")
    int getAttractionRewardPoints(@RequestParam String attractionId, @RequestParam String userId);

}
