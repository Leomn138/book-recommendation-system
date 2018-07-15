package com.bookrecommendationsystem.recommendation.repository;

import com.bookrecommendationsystem.recommendation.domain.*;
import com.bookrecommendationsystem.recommendation.stub.BookStub;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookRepositoryTest {

	@Autowired
	private BookRepository repository;

	@Test
	public void shouldFindBookByAsin() {

		Book stub = BookStub.get();
		repository.save(stub);

		Book found = repository.findByAsin(stub.getAsin());
		assertEquals(stub.getAuthor(), found.getAuthor());
		assertEquals(stub.getGenre(), found.getGenre());
        assertEquals(stub.getTitle(), found.getTitle());
	}
}
