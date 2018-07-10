package com.bookrecommendationsystem.recommendation.service;

import com.bookrecommendationsystem.recommendation.domain.Book;
import com.bookrecommendationsystem.recommendation.domain.Rating;
import com.bookrecommendationsystem.recommendation.domain.RatingLevel;
import com.bookrecommendationsystem.recommendation.domain.User;
import com.bookrecommendationsystem.recommendation.dto.*;
import com.bookrecommendationsystem.recommendation.repository.BookRepository;
import com.bookrecommendationsystem.recommendation.repository.RatingRepository;
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
    private BookService bookService;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private RecommendationService recommendationService;

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
        try {
            List<Integer> asinList = recommendationService.getTwentyRecommendationsForAnUser(Integer.parseInt(user.getId().toString()));
            Set<BookResponseV1> bookSet = new HashSet<>();
            for (Integer asin : asinList) {
                BookResponseV1 book = bookService.findByAsin(asin.toString());
                if (book != null)
                    bookSet.add(book);
            }
            return mapBookSetToRecommendationV1Response(bookSet);
        } catch (Exception e) {
            Set<BookResponseV1> bookSet = new HashSet<>();
            List<Book> mostRelevantBooks = ratingRepository.findMostRelevantBooks();
            for (int i = 0; i < 20 ; i++) {
                if (i < mostRelevantBooks.size()) {
                    BookResponseV1 bookResponse = mapBookToBookResponseV1(mostRelevantBooks.get(i));
                    bookSet.add(bookResponse);
                }
            }
            return mapBookSetToRecommendationV1Response(bookSet);
        }
    }

    @Override
    public RatingResponseV1 createNewRating(String username, RatingPostRequestV1 request) {
        return upsertRating(username, request.getAsin(), request.getRatingLevel());
    }

    @Override
    public RatingResponseV1 upsertRating(String username, String asin, RatingPutRequestV1 request) {
        return upsertRating(username, asin, request.getRatingLevel());
    }

    private RatingResponseV1 upsertRating(String username, String asin, String ratingLevel) {
        User user = userRepository.findByUsername(username);
        if (user == null)
            return fillRatingResponseWithUsernameNotFoundError(username);
        Book book = bookRepository.findByAsin(asin);
        if (book == null)
            return fillRatingResponseWithAsinNotFoundError(asin);

        if (!RatingLevel.isMember(ratingLevel)) {
            return fillRatingResponseWithRatingLevelBadRequestError(ratingLevel);
        }

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

    private UserResponseV1 mapUserToUserResponseV1(User user) {
        UserResponseV1 userResponse = new UserResponseV1();
        userResponse.setName(user.getName());
        userResponse.setUsername(user.getUsername());
        userResponse.setStatusCode(HttpStatus.CREATED);
        return userResponse;
    }

    private RatingResponseV1 mapRatingToRatingResponseV1(Rating rating) {
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

    private RecommendationResponseV1 mapBookSetToRecommendationV1Response(Set<BookResponseV1> bookSet) {
        RecommendationResponseV1 recommendationResponse = new RecommendationResponseV1();
        recommendationResponse.setStatusCode(HttpStatus.OK);
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