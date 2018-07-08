package com.bookrecommendationsystem.recommendation.service;

import com.bookrecommendationsystem.recommendation.dto.BookResponseV1;
import com.bookrecommendationsystem.recommendation.exception.BookNotFoundException;
import java.util.List;

public interface BookService {
	BookResponseV1 findByAsin(String asin) throws BookNotFoundException;
	List<BookResponseV1> getAll();
}
