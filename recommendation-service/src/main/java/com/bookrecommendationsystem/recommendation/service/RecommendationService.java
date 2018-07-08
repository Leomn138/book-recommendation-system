package com.bookrecommendationsystem.recommendation.service;

import com.bookrecommendationsystem.recommendation.domain.Book;
import com.bookrecommendationsystem.recommendation.domain.Rating;
import com.bookrecommendationsystem.recommendation.domain.User;

import java.util.List;

public interface RecommendationService {
    void calculateAll(List<Rating> ratings);
    List<Book> calculateSingle(User user);
    List<Book> getSingle(User user);
}
