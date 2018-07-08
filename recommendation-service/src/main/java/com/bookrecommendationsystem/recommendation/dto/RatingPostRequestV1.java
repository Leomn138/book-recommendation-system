package com.bookrecommendationsystem.recommendation.dto;

import javax.validation.constraints.NotNull;

public class RatingPostRequestV1 {
    @NotNull
    private String asin;
    @NotNull
    private String ratingLevel;

    public String getAsin() {
        return asin;
    }
    public void setAsin(String asin) {
        this.asin = asin;
    }

    public String getRatingLevel() {
        return ratingLevel;
    }
    public void setRatingLevel(String ratingLevel) {
        this.ratingLevel = ratingLevel;
    }
}
