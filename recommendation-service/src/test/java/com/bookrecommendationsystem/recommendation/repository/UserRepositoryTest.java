package com.bookrecommendationsystem.recommendation.repository;

import com.bookrecommendationsystem.recommendation.domain.User;
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
public class UserRepositoryTest {

	@Autowired
	private UserRepository repository;

	@Before
	public void removeAllRows() {
		repository.deleteAll();
	}

	@Test
	public void shouldFindUserByUsername() {

		User stub = UserStub.get();
		repository.save(stub);

		User found = repository.findByUsername(stub.getUsername());
		assertEquals(stub.getName(), found.getName());
	}
}
