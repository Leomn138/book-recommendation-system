package com.bookrecommendationsystem.recommendation.service;

import com.bookrecommendationsystem.recommendation.domain.Book;
import com.bookrecommendationsystem.recommendation.domain.Recommendation;
import com.bookrecommendationsystem.recommendation.domain.UserBookRating;
import com.bookrecommendationsystem.recommendation.domain.User;
import com.bookrecommendationsystem.recommendation.dto.*;
import com.bookrecommendationsystem.recommendation.repository.BookRepository;
import com.bookrecommendationsystem.recommendation.repository.RatingRepository;
import com.bookrecommendationsystem.recommendation.repository.RecommendationRepository;
import com.bookrecommendationsystem.recommendation.repository.UserRepository;
import com.bookrecommendationsystem.recommendation.stub.BookStub;
import com.bookrecommendationsystem.recommendation.stub.RatingStub;
import com.bookrecommendationsystem.recommendation.stub.RecommendationStub;
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
    private RecommendationRepository recommendationRepository;

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
        final UserBookRating rating = RatingStub.get();
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
    public void userService_CreateRating_WhenUsernameDoesNotExist_ShouldReturnNotFoundResponse(){
        final RatingPostRequestV1 ratingPostRequest = RatingStub.getPostRequest();

        when(userRepository.findByUsername(any())).thenReturn(null);

        RatingResponseV1 found = userService.createNewRating("123", ratingPostRequest);

        assertEquals(HttpStatus.NOT_FOUND, found.getStatusCode());
        assertEquals("Username 123 not found.", found.getError().getMessage());
    }

    @Test
    public void userService_CreateRating_WhenAsinDoesNotExist_ShouldReturnNotFoundResponse(){
        final User user = UserStub.get();
        final RatingPostRequestV1 ratingPostRequest = RatingStub.getPostRequest();

        when(userRepository.findByUsername(any())).thenReturn(user);
        when(bookRepository.findByAsin(any())).thenReturn(null);

        RatingResponseV1 found = userService.createNewRating(user.getUsername(), ratingPostRequest);

        assertEquals(HttpStatus.NOT_FOUND, found.getStatusCode());
        assertEquals("Book asin: 1234 not found.", found.getError().getMessage());
    }

    @Test
    public void userService_CreateRating_WhenRatingLevelDoesNotExist_ShouldReturnBadRequestResponse() {
        final UserBookRating rating = RatingStub.get();
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
    public void userService_UpdateRating_ShouldReturnUpsertRatingResponse() {
        final UserBookRating rating = RatingStub.get();
        final RatingPutRequestV1 ratingPutRequest = RatingStub.getPutRequest();

        when(userRepository.findByUsername(any())).thenReturn(rating.getUser());
        when(bookRepository.findByAsin(any())).thenReturn(rating.getBook());
        when(ratingRepository.findByUserAndBook(any(), any())).thenReturn(rating);
        when(ratingRepository.save(any())).thenReturn(rating);

        RatingResponseV1 found = userService.updateRating(rating.getUser().getUsername(), rating.getBook().getAsin(), ratingPutRequest);

        assertEquals(rating.getBook().getAsin(), found.getAsin());
        assertEquals(rating.getRatingLevel(), found.getRatingLevel());
        assertEquals(rating.getUser().getUsername(), found.getUsername());
        assertEquals(HttpStatus.CREATED, found.getStatusCode());
        assertNull(found.getError());
    }

    @Test
    public void userService_UpdateRating_WhenUsernameDoesNotExist_ShouldReturnNotFoundResponse(){
        final RatingPutRequestV1 ratingPutRequest = RatingStub.getPutRequest();

        when(userRepository.findByUsername(any())).thenReturn(null);

        RatingResponseV1 found = userService.updateRating("123", "1234", ratingPutRequest);

        assertEquals(HttpStatus.NOT_FOUND, found.getStatusCode());
        assertEquals("Username 123 not found.", found.getError().getMessage());
    }

    @Test
    public void userService_UpdateRating_WhenAsinDoesNotExist_ShouldReturnNotFoundResponse(){
        final User user = UserStub.get();
        final RatingPutRequestV1 ratingPutRequest = RatingStub.getPutRequest();

        when(userRepository.findByUsername(any())).thenReturn(user);
        when(bookRepository.findByAsin(any())).thenReturn(null);

        RatingResponseV1 found = userService.updateRating(user.getUsername(), "1234", ratingPutRequest);

        assertEquals(HttpStatus.NOT_FOUND, found.getStatusCode());
        assertEquals("Book asin: 1234 not found.", found.getError().getMessage());
    }

    @Test
    public void userService_UpdateRating_WhenRatingLevelDoesNotExist_ShouldReturnBadRequestResponse() {
        final UserBookRating rating = RatingStub.get();
        RatingPutRequestV1 ratingPutRequest = new RatingPutRequestV1();
        ratingPutRequest.setRatingLevel("NOT_EXISTS");

        when(userRepository.findByUsername(any())).thenReturn(rating.getUser());
        when(bookRepository.findByAsin(any())).thenReturn(rating.getBook());
        when(ratingRepository.findByUserAndBook(any(), any())).thenReturn(rating);

        RatingResponseV1 found = userService.updateRating(rating.getUser().getUsername(), rating.getBook().getAsin(), ratingPutRequest);

        assertEquals(HttpStatus.BAD_REQUEST, found.getStatusCode());
        assertEquals("RatingLevel: NOT_EXISTS does not exist. Choose LIKE, DISLIKE or NOT_INTERESTED", found.getError().getMessage());
    }

    @Test
    public void userService_GetRecommendations_ShouldReturnListOfBooksResponse() {
        List<Integer> asinList = new ArrayList<>();
        asinList.add(321331331);
        asinList.add(456);

        BookResponseV1 book = BookStub.getResponse();

        Recommendation recommendation = RecommendationStub.get();
        List<Recommendation> recommendationList = new ArrayList<>();
        recommendationList.add(recommendation);

        when(bookService.findByAsin("321331331")).thenReturn(book);
        when(bookService.findByAsin("456")).thenReturn(null);
        when(userRepository.findByUsername(any())).thenReturn(recommendation.getUser());
        when(recommendationRepository.findAllByUser(any())).thenReturn(recommendationList);

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
    public void userService_GetRecommendations_WhenRecommendationServiceThrowsException_ShouldReturnMostRelevantsBookResponse() {
        Book book = BookStub.get();
        List<Book> bookList = new ArrayList<>();
        bookList.add(book);
        Recommendation recommendation = RecommendationStub.get();
        List<Recommendation> recommendationList = new ArrayList<>();
        recommendationList.add(recommendation);

        when(userRepository.findByUsername(any())).thenReturn(recommendation.getUser());
        when(ratingRepository.findMostRelevantBooks()).thenReturn(bookList);
        when(recommendationRepository.findAllByUser(any())).thenReturn(null);

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
