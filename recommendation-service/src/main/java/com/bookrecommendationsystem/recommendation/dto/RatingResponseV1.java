package com.bookrecommendationsystem.recommendation.dto;

import com.bookrecommendationsystem.recommendation.domain.Book;
import com.bookrecommendationsystem.recommendation.domain.User;

public class RatingResponseV1 {
    String asin;
    String username;
    String ratingLevel;

    public String getAsin() {
        return asin;
    }
    public void setAsin(String asin) {
        this.asin = asin;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getRatingLevel() {
        return ratingLevel;
    }
    public void setRatingLevel(String ratingLevel) {
        this.ratingLevel = ratingLevel;
    }
}
