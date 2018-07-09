package com.bookrecommendationsystem.recommendation.domain;

public enum RatingLevel {
    DISLIKED(-10),
    LIKED(10),
    NOT_INTERESTED(0);

    private final int weight;
    RatingLevel(int weight) {
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }

    static public boolean isMember(String candidateRatingLevel) {
        RatingLevel[] existingRatingLevelsArray = RatingLevel.values();
        for (RatingLevel existingRatingLevel : existingRatingLevelsArray)
            if (existingRatingLevel.name().equals(candidateRatingLevel)) {
                return true;
            }
        return false;
    }
}
