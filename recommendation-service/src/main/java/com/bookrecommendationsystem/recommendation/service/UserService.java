package com.bookrecommendationsystem.recommendation.service;

import com.bookrecommendationsystem.recommendation.domain.Book;
import com.bookrecommendationsystem.recommendation.domain.Rating;
import com.bookrecommendationsystem.recommendation.domain.User;
import com.bookrecommendationsystem.recommendation.dto.*;
import com.bookrecommendationsystem.recommendation.exception.BookNotFoundException;
import com.bookrecommendationsystem.recommendation.exception.UserNotFoundException;

import java.util.List;
import java.util.Set;

public interface UserService {
    UserResponseV1 createNewUser(UserRequestV1 user) throws IllegalArgumentException;
    Set<BookResponseV1> getRecommendations(String username) throws UserNotFoundException;
    RatingResponseV1 upsertRating(String username, RatingPostRequestV1 request) throws UserNotFoundException, BookNotFoundException, IllegalArgumentException;
    RatingResponseV1 upsertRating(String username, String asin, RatingPutRequestV1 request) throws UserNotFoundException, BookNotFoundException, IllegalArgumentException;
    List<RatingResponseV1> getAllRatings();
}
