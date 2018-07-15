package com.bookrecommendationsystem.recommendation.repository;

import com.bookrecommendationsystem.recommendation.domain.Book;
import com.bookrecommendationsystem.recommendation.domain.Recommendation;
import com.bookrecommendationsystem.recommendation.domain.User;
import com.bookrecommendationsystem.recommendation.stub.BookStub;
import com.bookrecommendationsystem.recommendation.stub.UserStub;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.List;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RecommendationRepositoryTest {
    @Autowired
    private RecommendationRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;
    @Test
    public void shouldFindAllRecommendedBooksByUser() {
        Book book = BookStub.get();
        bookRepository.save(book);
        User user = UserStub.get();
        userRepository.save(user);
        Recommendation stub = new Recommendation();
        stub.setUser(user);
        stub.setBook(book);
        stub.setRating(10);
        repository.save(stub);

        List<Recommendation> found = repository.findAllByUser(user);

        assertEquals(book.getAsin(), found.get(0).getBook().getAsin());
    }

    @Test
    public void shouldDeleteByUser() {
        Book book = BookStub.get();
        bookRepository.save(book);
        User user = UserStub.get();
        userRepository.save(user);
        Recommendation stub = new Recommendation();
        stub.setUser(user);
        stub.setBook(book);
        stub.setRating(10);
        repository.save(stub);

        repository.deleteByUser(user);
        List<Recommendation> found = repository.findAllByUser(user);

        assertEquals(0, found.size());
    }
}
