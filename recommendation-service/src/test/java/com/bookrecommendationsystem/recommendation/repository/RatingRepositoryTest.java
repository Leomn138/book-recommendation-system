package com.bookrecommendationsystem.recommendation.repository;

import com.bookrecommendationsystem.recommendation.domain.Book;
import com.bookrecommendationsystem.recommendation.domain.UserBookRating;
import com.bookrecommendationsystem.recommendation.domain.User;
import com.bookrecommendationsystem.recommendation.stub.BookStub;
import com.bookrecommendationsystem.recommendation.stub.UserStub;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RatingRepositoryTest {

	@Autowired
	private RatingRepository repository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BookRepository bookRepository;

	@Test
	public void shouldFindRatingByUserAndBook() {
		Book book = BookStub.get();
		bookRepository.save(book);
		User user = UserStub.get();
		userRepository.save(user);
		UserBookRating stub = new UserBookRating();
		stub.setUser(user);
		stub.setBook(book);
		stub.setRatingLevel("LIKED");
		repository.save(stub);

		UserBookRating found = repository.findByUserAndBook(stub.getUser(), stub.getBook());
		assertEquals(user.getUsername(), found.getUser().getUsername());
		assertEquals(book.getAsin(), found.getBook().getAsin());
		assertEquals(stub.getRatingLevel(), found.getRatingLevel());
	}

	@Test
	public void shouldFindMostRelevantBooks() {
		Book book = BookStub.get();
		bookRepository.save(book);
		User user = UserStub.get();
		userRepository.save(user);
		UserBookRating stub = new UserBookRating();
		stub.setUser(user);
		stub.setBook(book);
		stub.setRatingLevel("LIKED");
		repository.save(stub);

		List<Book> found = repository.findMostRelevantBooks();

		assertEquals(book.getAsin(), found.get(0).getAsin());
	}
}
