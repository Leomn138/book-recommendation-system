package com.bookrecommendationsystem.recommendation.domain;

public enum RatingLevel {
    DISLIKED(0),
    LIKED(10),
    NOT_INTERESTED(4);

    private final int weight;
    RatingLevel(int weight) {
        this.weight = weight;
    }

    public int getWeight() { return weight; }
}
