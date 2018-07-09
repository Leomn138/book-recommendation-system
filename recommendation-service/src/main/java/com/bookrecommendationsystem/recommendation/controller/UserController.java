package com.bookrecommendationsystem.recommendation.controller;

import com.bookrecommendationsystem.recommendation.dto.*;
import com.bookrecommendationsystem.recommendation.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
public class UserController {

    @Autowired
    private UserService userService;
    private final Logger log = LoggerFactory.getLogger(getClass());

    @RequestMapping(path = "/v1/users", method = RequestMethod.POST)
    public ResponseEntity<UserResponseV1> createNewUser(@RequestBody UserRequestV1 user) {
        log.info("UserController - Started CreateNewUser username: " + user.getUsername() + ". " + new Date());
        try {
            UserResponseV1 userResponse = userService.createNewUser(user);
            log.info("UserController - Finished CreateNewUser username: " + user.getUsername() + ". " + new Date());
            return new ResponseEntity<>(userResponse, userResponse.getStatusCode());
        } catch (Exception e) {
            log.error("UserController - Error CreateNewUser username: " + user.getUsername() + ". " + new Date(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(path = "/v1/users/{username}/ratings", method = RequestMethod.POST)
    public ResponseEntity<RatingResponseV1> createNewRating(@PathVariable String username, @RequestBody RatingPostRequestV1 request) {
        log.info("UserController - Started CreateNewUser username: " + username + " asin: " + request.getAsin() + " ratingLevel: " + request.getRatingLevel() + ". " + new Date());
        try {
            RatingResponseV1 ratingResponse = userService.createNewRating(username, request);
            log.info("UserController - Finished CreateNewUser username: " + username + " asin: " + request.getAsin() + " ratingLevel: " + request.getRatingLevel() + ". " + new Date());
            return new ResponseEntity<>(ratingResponse, ratingResponse.getStatusCode());
        } catch (Exception e) {
            log.error("UserController - Error CreateNewUser username: " + username + " asin: " + request.getAsin() + " ratingLevel: " + request.getRatingLevel() + ". " + new Date(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(path = "/v1/users/{username}/ratings/{asin}", method = RequestMethod.PUT)
    public ResponseEntity<RatingResponseV1> updateRating(@PathVariable String username, @PathVariable String asin, @RequestBody RatingPutRequestV1 request) {
        log.info("UserController - Started CreateNewUser username: " + username + " asin: " + asin + " ratingLevel: " + request.getRatingLevel() + ". " + new Date());
        try {
            RatingResponseV1 ratingResponse = userService.upsertRating(username, asin, request);
            log.info("UserController - Finished CreateNewUser username: " + username + " asin: " + asin + " ratingLevel: " + request.getRatingLevel() + ". " + new Date());
            return new ResponseEntity<>(ratingResponse, ratingResponse.getStatusCode());
        } catch (Exception e) {
            log.error("UserController - Error CreateNewUser username: " + username + " asin: " + asin + " ratingLevel: " + request.getRatingLevel() + ". " + new Date(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(path = "/v1/users/{username}/recommendations", method = RequestMethod.GET)
    public ResponseEntity getTopRecommendations(@PathVariable String username) {
        log.info("UserController - Started GetTopRecommendations username: " + username + ". " + new Date());
        try {
            RecommendationResponseV1 recommendationResponse = userService.getRecommendations(username);
            log.info("UserController - Finished GetTopRecommendations username: " + username + ". " + new Date());
            return new ResponseEntity<>(recommendationResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("UserController - Error GetTopRecommendations username: " + username + ". " + new Date(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}