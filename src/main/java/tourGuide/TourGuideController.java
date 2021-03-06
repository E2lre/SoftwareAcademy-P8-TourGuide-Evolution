package tourGuide;

import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.jsoniter.output.JsonStream;

import tourGuide.model.external.VisitedLocation;

import tourGuide.exceptions.UserNameNotFoundException;
//import tourGuide.exceptions.UserPreferenceEmptyException;
import tourGuide.model.NearestAttractionsForUser;
import tourGuide.model.UserCurentLocation;
import tourGuide.model.UserPreferenceDTO;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;
import tourGuide.model.external.Provider;

@RestController
public class TourGuideController {

    private Logger logger = LoggerFactory.getLogger(TourGuideController.class);

    @Autowired
	TourGuideService tourGuideService;
	
    @RequestMapping("/")
    public String index() {
        return "Greetings from TourGuide!";
    }

    /**
     * get location for a user
     * @param userName user Name to get location
     * @return lorcation for the user
     */
    @RequestMapping("/getLocation")
    public String getLocation(@RequestParam String userName) {
        logger.info("Start getLocation");
    	VisitedLocation visitedLocation = tourGuideService.getUserLocation(getUser(userName));
		return JsonStream.serialize(visitedLocation.location);
    }
    
    //  TODO: Change this method to no longer return a List of Attractions.==> Done
 	//  Instead: Get the closest five tourist attractions to the user - no matter how far away they are.
 	//  Return a new JSON object that contains:
    	// Name of Tourist attraction, 
        // Tourist attractions lat/long, 
        // The user's location lat/long, 
        // The distance in miles between the user's location and each of the attractions.
        // The reward points for visiting each Attraction.
        //    Note: Attraction reward points can be gathered from RewardsCentral

    /**
     * get Near by Attraction for a user
     * @param userName User name to get near by attraction
     * @return near by attraction for the user
     */
    @RequestMapping("/getNearbyAttractions")
    public NearestAttractionsForUser getNearbyAttractionsV2(@RequestParam String userName) {
        NearestAttractionsForUser nearestAttractionsForUserResult;
        logger.info("Start getNearbyAttractions");
        User user = getUser(userName);
        VisitedLocation visitedLocation = tourGuideService.getUserLocation(getUser(userName));
        nearestAttractionsForUserResult = tourGuideService.getNearByAttractionsForUSer(user, visitedLocation);

        return nearestAttractionsForUserResult;

    }

    /**
     * get reward for a user
     * @param userName user name to get reward
     * @return reward sor the user
     */
    @RequestMapping("/getRewards") 
    public String getRewards(@RequestParam String userName) {
        logger.info("Start getRewards");
    	return JsonStream.serialize(tourGuideService.getUserRewards(getUser(userName)));
    }

    /**
     * get all current location for all users
      * @return all current locations
     */
    @RequestMapping("/getAllCurrentLocations")
    public List<UserCurentLocation> getAllCurrentLocations() {
    	// TODO: Get a list of every user's most recent location as JSON ==> Done
    	//- Note: does not use gpsUtil to query for their current location, 
    	//        but rather gathers the user's current location from their stored location history.
    	//
    	// Return object should be the just a JSON mapping of userId to Locations similar to:
    	//     {
    	//        "019b04a9-067a-4c76-8817-ee75088c3822": {"longitude":-48.188821,"latitude":74.84371} 
    	//        ...
    	//     }
        logger.info("Start getAllCurrentLocations");
    	return tourGuideService.getAllCurrentLocations();
    }

    /**
     * get trip deals for a user
     * @param userName user name to get trip deals
     * @return trip deals for the user
     */
    @RequestMapping("/getTripDeals")
    public List<Provider> getTripDeals(@RequestParam String userName) {
        logger.info("Start getTripDeals");
        List<Provider> providers = tourGuideService.getTripDeals(getUser(userName));
        return providers ;
    }

    @RequestMapping("/getUserPreference")
    @ResponseStatus(HttpStatus.OK)
    public UserPreferenceDTO getUserPreference(@RequestParam String userName) throws UserNameNotFoundException {
        logger.info("Start getUserPreference");
        if (tourGuideService.getUser(userName) == null ) {
            String message = " this username does not exist : "+ userName;
            logger.error(message);
            throw new UserNameNotFoundException(message);
        }
        return tourGuideService.getUserPreference (userName);

    }

    /**
     * Post user predference for a user
     * @param userName user name to update
     * @param userPreference new user préférence
     * @return user preference updated
     * @throws UserNameNotFoundException user name not found
     */
    @PostMapping("/setUserPreference")
    @ResponseStatus(HttpStatus.ACCEPTED)
    //public UserPreferenceDTO setUserPreference(@RequestParam String userName, @RequestBody UserPreferenceDTO userPreference) throws UserNameNotFoundException, UserPreferenceEmptyException {
    public UserPreferenceDTO setUserPreference(@RequestParam String userName, @RequestBody UserPreferenceDTO userPreference) throws UserNameNotFoundException {
        if (tourGuideService.getUser(userName) == null ) {
            String message = " this username does not exist : "+ userName;
            logger.error(message);
            throw new UserNameNotFoundException(message);
        }
        return tourGuideService.setUserPreference(userName,userPreference);
    }

    private User getUser(String userName) {
    	return tourGuideService.getUser(userName);
    }
   

}