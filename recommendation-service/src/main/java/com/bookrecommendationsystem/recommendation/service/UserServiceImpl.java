package com.bookrecommendationsystem.recommendation.service;

import com.bookrecommendationsystem.recommendation.domain.Book;
import com.bookrecommendationsystem.recommendation.domain.Rating;
import com.bookrecommendationsystem.recommendation.domain.RatingLevel;
import com.bookrecommendationsystem.recommendation.domain.User;
import com.bookrecommendationsystem.recommendation.dto.*;
import com.bookrecommendationsystem.recommendation.exception.BookNotFoundException;
import com.bookrecommendationsystem.recommendation.exception.UserNotFoundException;
import com.bookrecommendationsystem.recommendation.repository.BookRepository;
import com.bookrecommendationsystem.recommendation.repository.RatingRepository;
import com.bookrecommendationsystem.recommendation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private RecommendationService recommendationService;

    @Override
    public UserResponseV1 createNewUser(UserRequestV1 user) throws IllegalArgumentException {
        return createNewUser(user.getUsername(), user.getName());
    }

    private UserResponseV1 createNewUser(String username, String name) throws IllegalArgumentException {
        if (username == null || name == null) throw new IllegalArgumentException();

        User user = new User();
        user.setName(name);
        user.setUsername(username);
        userRepository.save(user);

        return mapUserToUserResponseV1(user);
    }

    @Override
    public Set<BookResponseV1> getRecommendations(String username) throws UserNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) throw new UserNotFoundException();
        return null;
    }

    @Override
    public RatingResponseV1 upsertRating(String username, RatingPostRequestV1 request) throws UserNotFoundException, BookNotFoundException, IllegalArgumentException {
        return upsertRating(username, request.getAsin(), request.getRatingLevel());
    }

    @Override
    public RatingResponseV1 upsertRating(String username, String asin, RatingPutRequestV1 request) throws UserNotFoundException, BookNotFoundException, IllegalArgumentException {
        return upsertRating(username, asin, request.getRatingLevel());
    }

    private RatingResponseV1 upsertRating(String username, String asin, String ratingLevel) throws UserNotFoundException, BookNotFoundException, IllegalArgumentException {
        User user = userRepository.findByUsername(username);
        if (user == null) throw new UserNotFoundException();
        Book book = bookRepository.findByAsin(asin);
        if (book == null) throw new BookNotFoundException();

        int ratingLevelWeight = RatingLevel.valueOf(ratingLevel).getWeight();

        Rating rating = ratingRepository.findByUserAndBook(user, book);
        if (rating == null) {
            rating = new Rating();
            rating.setBook(book);
            rating.setUser(user);
        }
        rating.setRatingLevel(ratingLevel);
        ratingRepository.save(rating);
        return mapRatingToRatingResponseV1(rating);
    }

    @Override
    public List<RatingResponseV1> getAllRatings() {
        return new ArrayList<RatingResponseV1>((Collection)ratingRepository.findAll());
    }

    private UserResponseV1 mapUserToUserResponseV1(User user) {
        UserResponseV1 userResponse = new UserResponseV1();
        userResponse.setName(user.getName());
        userResponse.setUsername(user.getUsername());
        return userResponse;
    }

    private RatingResponseV1 mapRatingToRatingResponseV1(Rating rating) {
        RatingResponseV1 ratingResponse = new RatingResponseV1();
        ratingResponse.setAsin(rating.getBook().getAsin());
        ratingResponse.setUsername(rating.getUser().getUsername());
        ratingResponse.setRatingLevel(rating.getRatingLevel());

        return ratingResponse;
    }
}