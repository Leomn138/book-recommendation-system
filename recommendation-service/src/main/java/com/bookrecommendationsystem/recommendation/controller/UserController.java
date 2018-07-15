package com.bookrecommendationsystem.recommendation.controller;

import com.bookrecommendationsystem.recommendation.dto.*;
import com.bookrecommendationsystem.recommendation.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
public class UserController {

    @Autowired
    private UserService userService;
    private final Logger log = LoggerFactory.getLogger(getClass());

    @RequestMapping(path = "/v1/users", method = RequestMethod.POST)
    public ResponseEntity<UserResponseV1> createNewUser(@RequestBody @Valid UserRequestV1 user) {
        log.info("Started CreateNewUser username: " + user.getUsername() + ".");
        try {
            UserResponseV1 userResponse = userService.createNewUser(user);
            log.info("Finished CreateNewUser username: " + user.getUsername() + ".");
            return new ResponseEntity<>(userResponse, userResponse.getStatusCode());
        } catch (Exception e) {
            log.error("Error CreateNewUser username: " + user.getUsername() + ".", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(path = "/v1/users/{username}/ratings", method = RequestMethod.POST)
    public ResponseEntity<RatingResponseV1> createNewRating(@PathVariable String username, @RequestBody @Valid RatingPostRequestV1 request) {
        log.info("Started CreateNewRating username: " + username + " asin: " + request.getAsin() + " ratingLevel: " + request.getRatingLevel() + ".");
        try {
            RatingResponseV1 ratingResponse = userService.createNewRating(username, request);
            log.info("Finished CreateNewRating username: " + username + " asin: " + request.getAsin() + " ratingLevel: " + request.getRatingLevel() + ".");
            return new ResponseEntity<>(ratingResponse, ratingResponse.getStatusCode());
        } catch (Exception e) {
            log.error("Error CreateNewRating username: " + username + " asin: " + request.getAsin() + " ratingLevel: " + request.getRatingLevel() + ".", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(path = "/v1/users/{username}/ratings/{asin}", method = RequestMethod.PUT)
    public ResponseEntity<RatingResponseV1> updateRating(@PathVariable String username, @PathVariable String asin, @RequestBody @Valid RatingPutRequestV1 request) {
        log.info("Started UpdateRating username: " + username + " asin: " + asin + " ratingLevel: " + request.getRatingLevel() + ".");
        try {
            RatingResponseV1 ratingResponse = userService.updateRating(username, asin, request);
            log.info("Finished UpdateRating username: " + username + " asin: " + asin + " ratingLevel: " + request.getRatingLevel() + ".");
            return new ResponseEntity<>(ratingResponse, ratingResponse.getStatusCode());
        } catch (Exception e) {
            log.error("Error UpdateRating username: " + username + " asin: " + asin + " ratingLevel: " + request.getRatingLevel() + ".", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(path = "/v1/users/{username}/recommendations", method = RequestMethod.GET)
    public ResponseEntity getTopRecommendations(@PathVariable String username) {
        log.info("Started GetTopRecommendations username: " + username + ".");
        try {
            RecommendationResponseV1 recommendationResponse = userService.getRecommendations(username);
            log.info("Finished GetTopRecommendations username: " + username + ".");
            return new ResponseEntity<>(recommendationResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error GetTopRecommendations username: " + username + ".", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}