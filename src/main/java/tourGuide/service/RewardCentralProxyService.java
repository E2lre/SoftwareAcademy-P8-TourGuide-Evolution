package tourGuide.service;

import org.springframework.web.bind.annotation.RequestParam;

public interface RewardCentralProxyService {
    public int getAttractionRewardPoints(String attractionId, String userId);

}
