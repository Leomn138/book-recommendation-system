package com.bookrecommendationsystem.recommendation.controller;

import com.bookrecommendationsystem.recommendation.dto.*;
import com.bookrecommendationsystem.recommendation.exception.BookNotFoundException;
import com.bookrecommendationsystem.recommendation.exception.UserNotFoundException;
import com.bookrecommendationsystem.recommendation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/v1/users", method = RequestMethod.POST)
    public ResponseEntity<UserResponseV1> createNewUser(@RequestBody UserRequestV1 user) {
        try {
            UserResponseV1 userResponse = userService.createNewUser(user);
            return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(path = "/v1/users/{username}/ratings", method = RequestMethod.POST)
    public ResponseEntity<RatingResponseV1> createNewRating(@PathVariable String username, @RequestBody RatingPostRequestV1 request) {
        try {
            RatingResponseV1 rating = userService.upsertRating(username, request);
            return new ResponseEntity<>(rating, HttpStatus.CREATED);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (BookNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(path = "/v1/users/{username}/ratings/{asin}", method = RequestMethod.PUT)
    public ResponseEntity<RatingResponseV1> updateRating(@PathVariable String username, @PathVariable String asin, @RequestBody RatingPutRequestV1 request) {
        try {
            RatingResponseV1 rating = userService.upsertRating(username, asin, request);
            return new ResponseEntity<>(rating, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (BookNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(path = "/v1/users/{username}/recommendations", method = RequestMethod.GET)
    public ResponseEntity getTopRecommendations(@PathVariable String username) {
        try {
            Set<BookResponseV1> books = userService.getRecommendations(username);
            return new ResponseEntity<>(books, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}