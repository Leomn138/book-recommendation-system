package com.bookrecommendationsystem.recommendation.dto;

import javax.validation.constraints.NotNull;

public class RatingPutRequestV1 {
    @NotNull
    private String ratingLevel;
    public String getRatingLevel() {
        return ratingLevel;
    }

    public void setRatingLevel(String ratingLevel) {
        this.ratingLevel = ratingLevel;
    }
}
