package tourGuide.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import tourGuide.proxies.GpsUtilProxy;
import tourGuide.proxies.RewardCentralProxy;

@Service
public class RewardCentralProxyServiceImpl implements RewardCentralProxyService {
    @Autowired
    private RewardCentralProxy rewardCentralProxy;
    @Override
    public int getAttractionRewardPoints(String attractionId, String userId) {

        return rewardCentralProxy.getAttractionRewardPoints(attractionId, userId);

    }


}
