package com.bookrecommendationsystem.recommendation.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "rating")
public class UserBookRating implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;


    private String ratingLevel;

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }
    public void setBook(Book book) {
        this.book = book;
    }

    public String getRatingLevel() {
        return ratingLevel;
    }
    public void setRatingLevel(String ratingLevel) {
        this.ratingLevel = ratingLevel;
    }
}