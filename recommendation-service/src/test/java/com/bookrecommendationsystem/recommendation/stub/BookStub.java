package com.bookrecommendationsystem.recommendation.stub;

import com.bookrecommendationsystem.recommendation.domain.Book;
import com.bookrecommendationsystem.recommendation.dto.BookResponseV1;
import com.bookrecommendationsystem.recommendation.dto.ErrorResponseV1;
import org.springframework.http.HttpStatus;

public class BookStub {
    public static Book get() {
        Book book = new Book();
        book.setAsin("321331331");
        book.setTitle("DDD");
        book.setAuthor("Eric Evans");
        book.setGenre("IT");
        return book;
    }

    public static BookResponseV1 getResponse() {
        BookResponseV1 book = new BookResponseV1();
        book.setAsin("321331331");
        book.setTitle("DDD");
        book.setAuthor("Eric Evans");
        book.setGenre("IT");
        book.setStatusCode(HttpStatus.OK);
        return book;
    }

    public static BookResponseV1 getBookNotFoundResponse() {
        ErrorResponseV1 error = new ErrorResponseV1();
        error.setMessage("Book asin: " + 123 + " not found.");
        BookResponseV1 bookResponse = new BookResponseV1();
        bookResponse.setStatusCode(HttpStatus.NOT_FOUND);
        bookResponse.setError(error);
        return bookResponse;
    }
}
