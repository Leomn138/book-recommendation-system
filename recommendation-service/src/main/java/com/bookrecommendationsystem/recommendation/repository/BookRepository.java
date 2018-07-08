package com.bookrecommendationsystem.recommendation.repository;

import com.bookrecommendationsystem.recommendation.domain.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends CrudRepository<Book, String> {
	Book findByAsin(String asin);
}
