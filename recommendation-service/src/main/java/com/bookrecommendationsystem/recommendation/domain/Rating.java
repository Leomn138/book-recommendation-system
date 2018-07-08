package com.bookrecommendationsystem.recommendation.domain;

import org.springframework.hateoas.ResourceSupport;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "rating")
public class Rating implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "username")
    private User user;

    @ManyToOne
    @JoinColumn(name = "asin")
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