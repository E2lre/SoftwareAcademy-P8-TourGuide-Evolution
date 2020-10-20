package tourGuide.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tourGuide.proxies.RewardCentralProxy;

/**
 * see REwardCentralProxy for more information
 */
@Service
public class RewardCentralProxyServiceImpl implements RewardCentralProxyService {
    @Autowired
    private RewardCentralProxy rewardCentralProxy;
    @Override
    public int getAttractionRewardPoints(String attractionId, String userId) {

        return rewardCentralProxy.getAttractionRewardPoints(attractionId, userId);

    }
}
