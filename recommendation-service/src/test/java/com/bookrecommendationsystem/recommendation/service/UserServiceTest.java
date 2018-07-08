package com.bookrecommendationsystem.recommendation.service;

import com.bookrecommendationsystem.recommendation.domain.Book;
import com.bookrecommendationsystem.recommendation.domain.Rating;
import com.bookrecommendationsystem.recommendation.domain.User;
import com.bookrecommendationsystem.recommendation.dto.*;
import com.bookrecommendationsystem.recommendation.exception.BookNotFoundException;
import com.bookrecommendationsystem.recommendation.exception.UserNotFoundException;
import com.bookrecommendationsystem.recommendation.repository.BookRepository;
import com.bookrecommendationsystem.recommendation.repository.RatingRepository;
import com.bookrecommendationsystem.recommendation.repository.UserRepository;
import com.bookrecommendationsystem.recommendation.stub.BookStub;
import com.bookrecommendationsystem.recommendation.stub.RatingStub;
import com.bookrecommendationsystem.recommendation.stub.UserStub;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserServiceTest {
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RatingRepository ratingRepository;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void shouldCreateNewUser() {

        final UserRequestV1 userRequest = UserStub.getRequest();
        final User user = UserStub.get();

        when(userRepository.save(any())).thenReturn(user);

        UserResponseV1 found = userService.createNewUser(userRequest);

        assertEquals(userRequest.getName(), found.getName());
        assertEquals(userRequest.getUsername(), found.getUsername());
    }

   @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenUsernameIsNullOnUserCreation() {
        final UserRequestV1 user = new UserRequestV1();

        userService.createNewUser(user);
    }

    @Test
    public void shouldCreateNewRating() throws BookNotFoundException, UserNotFoundException {
        final Rating rating = RatingStub.get();
        final RatingPostRequestV1 ratingPostRequest = RatingStub.getPostRequest();

        when(userRepository.findByUsername(any())).thenReturn(rating.getUser());
        when(bookRepository.findByAsin(any())).thenReturn(rating.getBook());
        when(ratingRepository.findByUserAndBook(any(), any())).thenReturn(null);
        when(ratingRepository.save(any())).thenReturn(rating);

        RatingResponseV1 found = userService.upsertRating(rating.getUser().getUsername(), ratingPostRequest);

        assertEquals(rating.getBook().getAsin(), found.getAsin());
        assertEquals(rating.getRatingLevel(), found.getRatingLevel());
        assertEquals(rating.getUser().getUsername(), found.getUsername());
    }

    @Test
    public void shouldUpdateRating() throws BookNotFoundException, UserNotFoundException {
        final Rating rating = RatingStub.get();
        final RatingPutRequestV1 ratingPutRequest = RatingStub.getPutRequest();

        when(userRepository.findByUsername(rating.getUser().getUsername())).thenReturn(rating.getUser());
        when(bookRepository.findByAsin(rating.getBook().getAsin())).thenReturn(rating.getBook());
        when(ratingRepository.findByUserAndBook(rating.getUser(), rating.getBook())).thenReturn(rating);
        when(ratingRepository.save(rating)).thenReturn(rating);

        RatingResponseV1 found = userService.upsertRating(rating.getUser().getUsername(), rating.getBook().getAsin(), ratingPutRequest);

        assertEquals(rating.getBook().getAsin(), found.getAsin());
        assertEquals(rating.getRatingLevel(), found.getRatingLevel());
        assertEquals(rating.getUser().getUsername(), found.getUsername());
    }

    @Test(expected = UserNotFoundException.class)
    public void shouldThrowUserNotFoundExceptionWhenUsernameDoesNotExistOnCreateNewRating() throws BookNotFoundException, UserNotFoundException {
        final RatingPostRequestV1 ratingPostRequest = RatingStub.getPostRequest();
        userService.upsertRating("123", ratingPostRequest);
    }

    @Test(expected = BookNotFoundException.class)
    public void shouldThrowBookNotFoundExceptionWhenAsinDoesNotExistOnCreateNewRating() throws BookNotFoundException, UserNotFoundException {
        final User user = UserStub.get();
        final RatingPostRequestV1 ratingPostRequest = RatingStub.getPostRequest();
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        userService.upsertRating(user.getUsername(), ratingPostRequest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenRatingLevelDoesNotExistOnRatingUpsert() throws BookNotFoundException, UserNotFoundException {
        final Rating rating = RatingStub.get();
        RatingPostRequestV1 ratingPostRequest = new RatingPostRequestV1();
        ratingPostRequest.setRatingLevel("NOT_EXIST");

        when(userRepository.findByUsername(any())).thenReturn(rating.getUser());
        when(bookRepository.findByAsin(any())).thenReturn(rating.getBook());
        when(ratingRepository.findByUserAndBook(any(), any())).thenReturn(rating);

        userService.upsertRating(rating.getUser().getUsername(), ratingPostRequest);
    }

    @Test
    public void shouldGetRecommendations() {


    }

    @Test(expected = UserNotFoundException.class)
    public void shouldThrowUserNotFoundExceptionWhenUsernameDoesNotExistOnGetRecommendations() throws UserNotFoundException {
        userService.getRecommendations("123");
    }
}
