package com.bookrecommendationsystem.recommendation.stub;

import com.bookrecommendationsystem.recommendation.domain.Book;
import com.bookrecommendationsystem.recommendation.domain.Rating;
import com.bookrecommendationsystem.recommendation.domain.User;
import com.bookrecommendationsystem.recommendation.dto.RatingPostRequestV1;
import com.bookrecommendationsystem.recommendation.dto.RatingPutRequestV1;
import com.bookrecommendationsystem.recommendation.dto.RatingResponseV1;

public class RatingStub {
    public static Rating get() {
        User userStub = UserStub.get();
        Book bookStub = BookStub.get();

        Rating rating = new Rating();
        rating.setBook(bookStub);
        rating.setUser(userStub);
        rating.setRatingLevel("LIKED");

        return rating;
    }

    public static RatingResponseV1 getResponse() {
        RatingResponseV1 rating = new RatingResponseV1();
        rating.setAsin("1234");
        rating.setUsername("leomn138");
        rating.setRatingLevel("LIKED");

        return rating;
    }

    public static RatingPutRequestV1 getPutRequest() {
        RatingPutRequestV1 rating = new RatingPutRequestV1();
        rating.setRatingLevel("LIKED");

        return rating;
    }

    public static RatingPostRequestV1 getPostRequest() {
        RatingPostRequestV1 rating = new RatingPostRequestV1();
        rating.setRatingLevel("LIKED");
        rating.setAsin("1234");

        return rating;
    }
}
