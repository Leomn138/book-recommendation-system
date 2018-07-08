package com.bookrecommendationsystem.recommendation.stub;

import com.bookrecommendationsystem.recommendation.domain.Book;
import com.bookrecommendationsystem.recommendation.dto.BookResponseV1;

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
        return book;
    }
}
