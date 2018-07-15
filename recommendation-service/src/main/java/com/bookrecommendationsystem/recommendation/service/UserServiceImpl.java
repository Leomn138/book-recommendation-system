package com.bookrecommendationsystem.recommendation.service;

import com.bookrecommendationsystem.recommendation.domain.*;
import com.bookrecommendationsystem.recommendation.dto.*;
import com.bookrecommendationsystem.recommendation.repository.BookRepository;
import com.bookrecommendationsystem.recommendation.repository.RatingRepository;
import com.bookrecommendationsystem.recommendation.repository.RecommendationRepository;
import com.bookrecommendationsystem.recommendation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private RecommendationRepository recommendationRepository;

    @Override
    public UserResponseV1 createNewUser(UserRequestV1 user) {
        return createNewUser(user.getUsername(), user.getName());
    }

    private UserResponseV1 createNewUser(String username, String name) {
        if (username == null || username.isEmpty() || name == null || name.isEmpty())
            return fillUserResponseWithUsernameBadRequestError();

        User user = new User();
        user.setName(name);
        user.setUsername(username);
        user = userRepository.save(user);

        return mapUserToUserResponseV1(user);
    }

    @Override
    public RecommendationResponseV1 getRecommendations(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null)
            return fillRecommendationResponseWithUsernameNotFoundError(username);
        List<Recommendation> recommendationList = recommendationRepository.findAllByUser(user);
        if (recommendationList != null && recommendationList.size() > 0)
            return mapRecommendationListToRecommendationV1Response(recommendationList);
        else
            return getMostRelevantBooks();
    }

    private RecommendationResponseV1 getMostRelevantBooks() {
        int numberOfRecommendations = 20;
        Set<BookResponseV1> mostRecommendedBookSet = new HashSet<>();
        List<Book> mostRelevantBooks = ratingRepository.findMostRelevantBooks();
        for (int i = 0; i < numberOfRecommendations ; i++) {
            if (i < mostRelevantBooks.size()) {
            BookResponseV1 bookResponse = mapBookToBookResponseV1(mostRelevantBooks.get(i));
                mostRecommendedBookSet.add(bookResponse);
            }
        }
        return mapBookSetToRecommendationV1Response(mostRecommendedBookSet);
    }
    @Override
    public RatingResponseV1 createNewRating(String username, RatingPostRequestV1 request) {
        User user = userRepository.findByUsername(username);
        if (user == null)
            return fillRatingResponseWithUsernameNotFoundError(username);
        Book book = bookRepository.findByAsin(request.getAsin());
        if (book == null)
            return fillRatingResponseWithAsinNotFoundError(request.getAsin());

        if (!RatingLevel.isMember(request.getRatingLevel())) {
            return fillRatingResponseWithRatingLevelBadRequestError(request.getRatingLevel());
        }

        UserBookRating rating = ratingRepository.findByUserAndBook(user, book);
        if (rating != null) {
            return fillRatingResponseWithRatingAlreadyExistsError(username, request.getAsin(), request.getRatingLevel());
        }
        rating = new UserBookRating();
        rating.setBook(book);
        rating.setUser(user);
        rating.setRatingLevel(request.getRatingLevel());
        ratingRepository.save(rating);
        return mapRatingToRatingResponseV1(rating);
    }

    @Override
    public RatingResponseV1 updateRating(String username, String asin, RatingPutRequestV1 request) {
        User user = userRepository.findByUsername(username);
        if (user == null)
            return fillRatingResponseWithUsernameNotFoundError(username);
        Book book = bookRepository.findByAsin(asin);
        if (book == null)
            return fillRatingResponseWithAsinNotFoundError(asin);

        if (!RatingLevel.isMember(request.getRatingLevel())) {
            return fillRatingResponseWithRatingLevelBadRequestError(request.getRatingLevel());
        }
        UserBookRating rating = ratingRepository.findByUserAndBook(user, book);
        if (rating == null) {
            return fillRatingResponseWithRatingNotFoundError(username, asin, request.getRatingLevel());
        }
        rating.setRatingLevel(request.getRatingLevel());
        ratingRepository.save(rating);
        return mapRatingToRatingResponseV1(rating);
    }

    private UserResponseV1 mapUserToUserResponseV1(User user) {
        UserResponseV1 userResponse = new UserResponseV1();
        userResponse.setName(user.getName());
        userResponse.setUsername(user.getUsername());
        userResponse.setStatusCode(HttpStatus.CREATED);
        return userResponse;
    }

    private RatingResponseV1 mapRatingToRatingResponseV1(UserBookRating rating) {
        RatingResponseV1 ratingResponse = new RatingResponseV1();
        ratingResponse.setAsin(rating.getBook().getAsin());
        ratingResponse.setUsername(rating.getUser().getUsername());
        ratingResponse.setRatingLevel(rating.getRatingLevel());
        ratingResponse.setStatusCode(HttpStatus.CREATED);

        return ratingResponse;
    }

    private UserResponseV1 fillUserResponseWithUsernameBadRequestError() {
        ErrorResponseV1 error = new ErrorResponseV1();
        error.setMessage("Username or name are not provided.");
        UserResponseV1 userResponse = new UserResponseV1();
        userResponse.setStatusCode(HttpStatus.BAD_REQUEST);
        userResponse.setError(error);
        return userResponse;
    }

    private RecommendationResponseV1 mapBookSetToRecommendationV1Response(Set<BookResponseV1> bookResponseSet) {
        RecommendationResponseV1 recommendationResponse = new RecommendationResponseV1();
        recommendationResponse.setStatusCode(HttpStatus.OK);
        recommendationResponse.setBooks(bookResponseSet);
        return recommendationResponse;
    }

    private RecommendationResponseV1 mapRecommendationListToRecommendationV1Response(List<Recommendation> recommendationList) {
        RecommendationResponseV1 recommendationResponse = new RecommendationResponseV1();
        recommendationResponse.setStatusCode(HttpStatus.OK);
        Set<BookResponseV1> bookSet = new HashSet<>();
        for (Recommendation recommendation : recommendationList) {
            bookSet.add(mapBookToBookResponseV1(recommendation.getBook()));
        }
        recommendationResponse.setBooks(bookSet);
        return recommendationResponse;
    }

    private RecommendationResponseV1 fillRecommendationResponseWithUsernameNotFoundError(String username) {
        ErrorResponseV1 error = new ErrorResponseV1();
        error.setMessage("Username " + username + " not found.");
        RecommendationResponseV1 recommendationResponse = new RecommendationResponseV1();
        recommendationResponse.setStatusCode(HttpStatus.NOT_FOUND);
        recommendationResponse.setError(error);
        return recommendationResponse;
    }

    private RatingResponseV1 fillRatingResponseWithUsernameNotFoundError(String username) {
        ErrorResponseV1 error = new ErrorResponseV1();
        error.setMessage("Username " + username + " not found.");
        RatingResponseV1 ratingResponse = new RatingResponseV1();
        ratingResponse.setStatusCode(HttpStatus.NOT_FOUND);
        ratingResponse.setError(error);
        return ratingResponse;
    }

    private RatingResponseV1 fillRatingResponseWithRatingNotFoundError(String username, String asin, String ratingLevel) {
        ErrorResponseV1 error = new ErrorResponseV1();
        error.setMessage("Rating " + username + ", " + asin + ", " + ratingLevel + " not found.");
        RatingResponseV1 ratingResponse = new RatingResponseV1();
        ratingResponse.setStatusCode(HttpStatus.NOT_FOUND);
        ratingResponse.setError(error);
        return ratingResponse;
    }

    private RatingResponseV1 fillRatingResponseWithRatingAlreadyExistsError(String username, String asin, String ratingLevel) {
        ErrorResponseV1 error = new ErrorResponseV1();
        error.setMessage("Rating " + username + ", " + asin + ", " + ratingLevel + " already exists.");
        RatingResponseV1 ratingResponse = new RatingResponseV1();
        ratingResponse.setStatusCode(HttpStatus.BAD_REQUEST);
        ratingResponse.setError(error);
        return ratingResponse;
    }

    private RatingResponseV1 fillRatingResponseWithAsinNotFoundError(String asin) {
        ErrorResponseV1 error = new ErrorResponseV1();
        error.setMessage("Book asin: " + asin + " not found.");
        RatingResponseV1 ratingResponse = new RatingResponseV1();
        ratingResponse.setStatusCode(HttpStatus.NOT_FOUND);
        ratingResponse.setError(error);
        return ratingResponse;
    }

    private RatingResponseV1 fillRatingResponseWithRatingLevelBadRequestError(String ratingLevel) {
        ErrorResponseV1 error = new ErrorResponseV1();
        error.setMessage("RatingLevel: " + ratingLevel + " does not exist. Choose LIKE, DISLIKE or NOT_INTERESTED");
        RatingResponseV1 ratingResponse = new RatingResponseV1();
        ratingResponse.setStatusCode(HttpStatus.BAD_REQUEST);
        ratingResponse.setError(error);
        return ratingResponse;
    }

    private BookResponseV1 mapBookToBookResponseV1(Book book) {
        BookResponseV1 bookResponse = new BookResponseV1();
        bookResponse.setGenre(book.getGenre());
        bookResponse.setAsin(book.getAsin());
        bookResponse.setAuthor(book.getAuthor());
        bookResponse.setTitle(book.getTitle());
        bookResponse.setStatusCode(HttpStatus.OK);
        return bookResponse;
    }
}