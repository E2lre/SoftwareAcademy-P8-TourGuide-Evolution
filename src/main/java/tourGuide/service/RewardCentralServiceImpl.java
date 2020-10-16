package tourGuide.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rewardCentral.RewardCentral;
import tourGuide.proxies.RewardCentralProxy;

import java.util.UUID;

@Service
public class RewardCentralServiceImpl implements RewardCentralService{

    @Autowired
    private RewardCentralProxy rewardsCentral;
    //private final RewardCentral rewardsCentral;

    public RewardCentralServiceImpl() {
        //this.rewardsCentral = new RewardCentralPro();
    }

    @Override
    public int getAttractionRewardPoints(UUID attractionId, UUID userId){
        //return rewardsCentral.getAttractionRewardPoints(attractionId,userId);
        return rewardsCentral.getAttractionRewardPoints(attractionId.toString(),userId.toString());
    }
}
