package tourGuide.model;

import tourGuide.model.external.Attraction;

/**
 * Give information about 1 nearest attraction
 */
public class NearestAttraction {

    private Attraction attraction;
    private Double distance;
    private int rewardPoints;

    public NearestAttraction(Attraction attraction, Double distance, int rewardPoints) {
        this.attraction = attraction;
        this.distance = distance;
        this.rewardPoints = rewardPoints;
    }

    public Attraction getAttraction() {
        return attraction;
    }

    public void setAttraction(Attraction attraction) {
        this.attraction = attraction;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public int getRewardPoints() {
        return rewardPoints;
    }

    public void setRewardPoints(int rewardPoints) {
        this.rewardPoints = rewardPoints;
    }

    @Override
    public String toString() {
        return "NearestAttraction{" +
                "attraction=" + attraction +
                ", distance=" + distance +
                ", rewardPoints=" + rewardPoints +
                '}';
    }

}
