/*
package tourGuide.service;

import tourGuide.beans.Attraction;
import tourGuide.beans.Location;
import tourGuide.beans.VisitedLocation;
import tourGuide.helper.InternalTestHelper;
import tourGuide.helper.Util;
import tourGuide.model.*;
import tourGuide.proxies.GpsUtilProxy;
import tourGuide.tracker.Tracker;
import tourGuide.user.User;
import tourGuide.user.UserPreferences;
import tourGuide.user.UserReward;
import tripPricer.Provider;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public interface TourGuideServiceInt {
    public void TourGuideService(GpsUtilProxy gpsUtil, RewardsService rewardsService);

    public List<UserReward> getUserRewards(User user);

    public VisitedLocation getUserLocation(User user);

    public List<UserCurentLocation> getAllCurrentLocations();

    public User getUser(String userName);

    public List<User> getAllUsers();

    public void addUser(User user) ;

    public List<Provider> getTripDeals(User user);

    public VisitedLocation trackUserLocation(User user);

    public void trackUserLocationList(List<User> users) ;

    public NearestAttractionsForUser getNearByAttractionsForUSer(User user, VisitedLocation visitedLocation) ;

    public UserPreferenceDTO getUserPreference(String userName);

    public UserPreferenceDTO setUserPreference(String userName, UserPreferenceDTO userPreference);


}
*/
