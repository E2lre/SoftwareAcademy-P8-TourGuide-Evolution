package tourGuide.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "rewardCentral", url="localhost:8081")
public interface RewardCentralProxy {
    @GetMapping(value ="/getAttractionRewardPoints")
    int getAttractionRewardPoints(@RequestParam String attractionId, @RequestParam String userId);

}
