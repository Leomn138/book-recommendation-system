package com.bookrecommendationsystem.recommendation.service;

import com.bookrecommendationsystem.recommendation.domain.Rating;

import java.util.List;

public interface RecommendationService {
    void init(String environment, String appName);
    void trainModel(List<Rating> ratings, int rank, int interactionsNumber, double lambda);
    List<Integer> getTwentyRecommendationsForAnUser(int userId);
}
