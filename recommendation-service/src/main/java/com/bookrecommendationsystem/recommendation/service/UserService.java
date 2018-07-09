package com.bookrecommendationsystem.recommendation.service;

import com.bookrecommendationsystem.recommendation.dto.*;

public interface UserService {
    UserResponseV1 createNewUser(UserRequestV1 user) throws IllegalArgumentException;
    RecommendationResponseV1 getRecommendations(String username);
    RatingResponseV1 createNewRating(String username, RatingPostRequestV1 request);
    RatingResponseV1 upsertRating(String username, String asin, RatingPutRequestV1 request);
}
