package com.bookrecommendationsystem.recommendation.service;

import com.bookrecommendationsystem.recommendation.domain.Book;
import com.bookrecommendationsystem.recommendation.domain.Rating;
import com.bookrecommendationsystem.recommendation.domain.User;
import com.bookrecommendationsystem.recommendation.dto.*;
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
import org.springframework.http.HttpStatus;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserServiceTest {
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookService bookService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private RecommendationService recommendationService;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void userService_CreateNewUser_ShouldReturnNewUserResponse() {

        final UserRequestV1 userRequest = UserStub.getRequest();
        final User user = UserStub.get();

        when(userRepository.save(any())).thenReturn(user);

        UserResponseV1 found = userService.createNewUser(userRequest);

        assertEquals(userRequest.getName(), found.getName());
        assertEquals(userRequest.getUsername(), found.getUsername());
        assertEquals(HttpStatus.CREATED, found.getStatusCode());
        assertNull(found.getError());
    }

   @Test
    public void userService_CreateNewUser_WhenUsernameIsNotFilled_ShouldReturnBadRequestError() {
        final UserRequestV1 user = new UserRequestV1();

       UserResponseV1 found = userService.createNewUser(user);

       assertEquals(HttpStatus.BAD_REQUEST, found.getStatusCode());
       assertEquals("Username or name are not provided.", found.getError().getMessage());
    }

    @Test
    public void userService_CreateNewRating_ShouldReturnUpsertRatingResponse() {
        final Rating rating = RatingStub.get();
        final RatingPostRequestV1 ratingPostRequest = RatingStub.getPostRequest();

        when(userRepository.findByUsername(any())).thenReturn(rating.getUser());
        when(bookRepository.findByAsin(any())).thenReturn(rating.getBook());
        when(ratingRepository.findByUserAndBook(any(), any())).thenReturn(null);
        when(ratingRepository.save(any())).thenReturn(rating);

        RatingResponseV1 found = userService.createNewRating(rating.getUser().getUsername(), ratingPostRequest);

        assertEquals(rating.getBook().getAsin(), found.getAsin());
        assertEquals(rating.getRatingLevel(), found.getRatingLevel());
        assertEquals(rating.getUser().getUsername(), found.getUsername());
        assertEquals(HttpStatus.CREATED, found.getStatusCode());
        assertNull(found.getError());
    }

    @Test
    public void userService_UpdateRating_ShouldReturnUpsertRatingResponse() {
        final Rating rating = RatingStub.get();
        final RatingPutRequestV1 ratingPutRequest = RatingStub.getPutRequest();

        when(userRepository.findByUsername(any())).thenReturn(rating.getUser());
        when(bookRepository.findByAsin(any())).thenReturn(rating.getBook());
        when(ratingRepository.findByUserAndBook(any(), any())).thenReturn(rating);
        when(ratingRepository.save(any())).thenReturn(rating);

        RatingResponseV1 found = userService.upsertRating(rating.getUser().getUsername(), rating.getBook().getAsin(), ratingPutRequest);

        assertEquals(rating.getBook().getAsin(), found.getAsin());
        assertEquals(rating.getRatingLevel(), found.getRatingLevel());
        assertEquals(rating.getUser().getUsername(), found.getUsername());
        assertEquals(HttpStatus.CREATED, found.getStatusCode());
        assertNull(found.getError());
    }

    @Test
    public void userService_UpsertRating_WhenUsernameDoesNotExist_ShouldReturnNotFoundResponse(){
        final RatingPostRequestV1 ratingPostRequest = RatingStub.getPostRequest();

        when(userRepository.findByUsername(any())).thenReturn(null);

        RatingResponseV1 found = userService.createNewRating("123", ratingPostRequest);

        assertEquals(HttpStatus.NOT_FOUND, found.getStatusCode());
        assertEquals("Username 123 not found.", found.getError().getMessage());
    }

    @Test
    public void userService_UpsertRating_WhenAsinDoesNotExist_ShouldReturnNotFoundResponse(){
        final User user = UserStub.get();
        final RatingPostRequestV1 ratingPostRequest = RatingStub.getPostRequest();

        when(userRepository.findByUsername(any())).thenReturn(user);
        when(bookRepository.findByAsin(any())).thenReturn(null);

        RatingResponseV1 found = userService.createNewRating(user.getUsername(), ratingPostRequest);

        assertEquals(HttpStatus.NOT_FOUND, found.getStatusCode());
        assertEquals("Book asin: 1234 not found.", found.getError().getMessage());
    }

    @Test
    public void userService_UpsertRating_WhenRatingLevelDoesNotExist_ShouldReturnBadRequestResponse() {
        final Rating rating = RatingStub.get();
        RatingPostRequestV1 ratingPostRequest = new RatingPostRequestV1();
        ratingPostRequest.setRatingLevel("NOT_EXISTS");

        when(userRepository.findByUsername(any())).thenReturn(rating.getUser());
        when(bookRepository.findByAsin(any())).thenReturn(rating.getBook());
        when(ratingRepository.findByUserAndBook(any(), any())).thenReturn(rating);

        RatingResponseV1 found = userService.createNewRating(rating.getUser().getUsername(), ratingPostRequest);

        assertEquals(HttpStatus.BAD_REQUEST, found.getStatusCode());
        assertEquals("RatingLevel: NOT_EXISTS does not exist. Choose LIKE, DISLIKE or NOT_INTERESTED", found.getError().getMessage());
    }

    @Test
    public void userService_GetRecommendations_ShouldReturnListOfBooksResponse() throws Exception {
        List<Integer> asinList = new ArrayList<>();
        asinList.add(321331331);
        asinList.add(456);

        BookResponseV1 book = BookStub.getResponse();
        User user = UserStub.get();

        when(recommendationService.getTwentyRecommendationsForAnUser(1)).thenReturn(asinList);
        when(bookService.findByAsin("321331331")).thenReturn(book);
        when(bookService.findByAsin("456")).thenReturn(null);
        when(userRepository.findByUsername(any())).thenReturn(user);

        RecommendationResponseV1 response = userService.getRecommendations("leomn138");

        assertEquals(1, response.getBooks().size());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getError());
        assertEquals(book.getAsin(), ((BookResponseV1)response.getBooks().toArray()[0]).getAsin());
        assertEquals(book.getAuthor(), ((BookResponseV1)response.getBooks().toArray()[0]).getAuthor());
        assertEquals(book.getTitle(), ((BookResponseV1)response.getBooks().toArray()[0]).getTitle());
        assertEquals(book.getGenre(), ((BookResponseV1)response.getBooks().toArray()[0]).getGenre());
    }

    @Test
    public void userService_GetRecommendations_WhenRecommendationServiceThrowsException_ShouldReturnMostRelevantsBookResponse() throws Exception {
        Book book = BookStub.get();
        List<Book> bookList = new ArrayList<>();
        bookList.add(book);
        User user = UserStub.get();

        when(recommendationService.getTwentyRecommendationsForAnUser(1)).thenThrow(new Exception());
        when(userRepository.findByUsername(any())).thenReturn(user);
        when(ratingRepository.findMostRelevantBooks()).thenReturn(bookList);
        RecommendationResponseV1 response = userService.getRecommendations("leomn138");

        assertEquals(1, response.getBooks().size());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getError());
        assertEquals(book.getAsin(), ((BookResponseV1)response.getBooks().toArray()[0]).getAsin());
        assertEquals(book.getAuthor(), ((BookResponseV1)response.getBooks().toArray()[0]).getAuthor());
        assertEquals(book.getTitle(), ((BookResponseV1)response.getBooks().toArray()[0]).getTitle());
        assertEquals(book.getGenre(), ((BookResponseV1)response.getBooks().toArray()[0]).getGenre());
    }

    @Test
    public void userService_GetRecommendations_WhenUsernameDoesNotExist_ShouldReturnNotFoundResponse() {
        when(userRepository.findByUsername(any())).thenReturn(null);
        RecommendationResponseV1 found = userService.getRecommendations("123");

        assertNull(found.getBooks());
        assertEquals(HttpStatus.NOT_FOUND, found.getStatusCode());
        assertEquals("Username 123 not found.", found.getError().getMessage());
    }
}
