package com.bookrecommendationsystem.recommendation.repository;

import com.bookrecommendationsystem.recommendation.domain.Book;
import com.bookrecommendationsystem.recommendation.domain.Rating;
import com.bookrecommendationsystem.recommendation.domain.User;
import com.bookrecommendationsystem.recommendation.stub.BookStub;
import com.bookrecommendationsystem.recommendation.stub.RatingStub;
import com.bookrecommendationsystem.recommendation.stub.UserStub;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
		Rating stub = new Rating();
		stub.setUser(user);
		stub.setBook(book);
		stub.setRatingLevel("LIKED");
		repository.save(stub);

		Rating found = repository.findByUserAndBook(stub.getUser(), stub.getBook());
		assertEquals(user.getUsername(), found.getUser().getUsername());
		assertEquals(book.getAsin(), found.getBook().getAsin());
		assertEquals(stub.getRatingLevel(), found.getRatingLevel());
	}
}
