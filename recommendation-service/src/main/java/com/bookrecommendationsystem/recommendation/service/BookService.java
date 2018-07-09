package com.bookrecommendationsystem.recommendation.service;

import com.bookrecommendationsystem.recommendation.dto.BookResponseV1;
import java.util.List;

public interface BookService {
	BookResponseV1 findByAsin(String asin);
	List<BookResponseV1> getAll();
}
