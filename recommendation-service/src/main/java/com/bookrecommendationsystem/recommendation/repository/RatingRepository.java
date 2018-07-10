package com.bookrecommendationsystem.recommendation.repository;

import com.bookrecommendationsystem.recommendation.domain.Book;
import com.bookrecommendationsystem.recommendation.domain.Rating;
import com.bookrecommendationsystem.recommendation.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends CrudRepository<Rating, String> {
    Rating findByUserAndBook(User user, Book book);

    @Query("select book from Rating group by book_id order by count(book_id) desc")
    List<Book> findMostRelevantBooks();
}
