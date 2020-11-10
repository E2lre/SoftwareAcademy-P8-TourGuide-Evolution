# TourGuide
API REST application with micro-service to managing travel and attraction for a custumer. 
This app use SPRINT BOOT, FeignClient.

## Getting Started

- Endpoint : http://localhost:8080/

## Prerequisites

What things you need to install the software and how to install them

- Java 1.8 or later
- Spring Boot 2.2.6
- Gradle 2.2.5
- Docker 2.5.0.0 (Optional)

# Installation

## No Database creation needed
   
## Without Docker
To start the application start sub-micro-service in build libs directories: 
- Gpsutil : java -jar gpsUtil-0.0.1-SNAPSHOT.jar
- RewardCentral : java -jar rewardCentral-0.0.1-SNAPSHOT.jar
- TripPricer : java -jar tripPricer-0.0.1-SNAPSHOT.jar

And start the application
- TourGuide : java -jar Tourguide-0.0.1-SNAPSHOT.jar


## With Docker

### Docker construction in project directory
- docker build --build-arg JAR_FILE=build/libs/*.jar -t tourguide .

### Start sub-micro-service
- Gpsutil : docker run -p 8082:8082 --name GpsUtil gpsutil
- RewardCentral : docker run -p 8083:8083 --name RewardCentral rewardcentral
- TripPricer : docker run -p 8084:8084 --name TripPricer trippricer

### Start TourGuide
- TourGuide : docker run -p 8080:8080 --name TourGuide tourguide

### Or use Docker compose to avoid the 2 last points
- execute command line to start all components: docker-compose up -d

# Architecture Diagram
![alt text](Architecture.png)


# URI
## Get Location
Example : 
GET http://localhost:8080/getLocation?userName=internalUser2

## Get Near By Attractions
Example : 
http://localhost:8080/getNearbyAttractions?userName=internalUser3

## Get Rewards
Example : 
http://localhost:8080/getRewards?userName=internalUser2

## Get All Current Locations
Example : 
http://localhost:8080/getAllCurrentLocations?userName=internalUser1

## Get Trip Deals
Example : 
http://localhost:8080/getTripDeals?userName=internalUser2

## Set User Preference
Example : 
http://localhost:8080/setUserPreference?userName=internalUser2

boby example :
{
    "attractionProximity": 4000,
    "currency": "USD",
    "lowerPricePoint": 
    {
        "currency":  "USD",
        "amount" : 0
    },
    "tripDuration": 1,
    "ticketQuantity": 1,
    "numberOfAdults": 1,
    "numberOfChildren": 0
}

## Get User Preference
Example : 
http://localhost:8080/getUserPreference?userName=internalUser2