package com.bookrecommendationsystem.recommendation.stub;

import com.bookrecommendationsystem.recommendation.domain.Book;
import com.bookrecommendationsystem.recommendation.domain.Recommendation;
import com.bookrecommendationsystem.recommendation.domain.User;
import com.bookrecommendationsystem.recommendation.dto.BookResponseV1;
import com.bookrecommendationsystem.recommendation.dto.RecommendationResponseV1;
import org.springframework.http.HttpStatus;
import java.util.HashSet;
import java.util.Set;

public class RecommendationStub {
    public static RecommendationResponseV1 getResponse() {
        Set<BookResponseV1> bookResponseList = new HashSet<>();
        bookResponseList.add(BookStub.getResponse());
        RecommendationResponseV1 recommendationResponse = new RecommendationResponseV1();
        recommendationResponse.setBooks(bookResponseList);
        recommendationResponse.setStatusCode(HttpStatus.OK);
        return recommendationResponse;
    }

    public static Recommendation get() {
        Book book = BookStub.get();
        User user = UserStub.get();
        Recommendation recommendation = new Recommendation();
        recommendation.setBook(book);
        recommendation.setUser(user);
        recommendation.setRating(10);
        return recommendation;
    }
}
