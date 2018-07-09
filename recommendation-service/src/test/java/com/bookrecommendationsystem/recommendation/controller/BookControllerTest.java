package com.bookrecommendationsystem.recommendation.controller;

import com.bookrecommendationsystem.recommendation.dto.BookResponseV1;
import com.bookrecommendationsystem.recommendation.stub.BookStub;
import com.bookrecommendationsystem.recommendation.domain.*;
import com.bookrecommendationsystem.recommendation.service.BookService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import javax.ws.rs.InternalServerErrorException;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BookControllerTest {

	@InjectMocks
	private BookController bookController;

	@Mock
	private BookService bookService;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
	}

	@Test
	public void bookController_GetBookByAsinNumber_ShouldReturnCorrectBook() throws Exception {

		final BookResponseV1 book = BookStub.getResponse();

		when(bookService.findByAsin(book.getAsin())).thenReturn(book);

		mockMvc.perform(get("/v1/books/" + book.getAsin()))
				.andExpect(jsonPath("$.asin").value(book.getAsin()))
				.andExpect(status().isOk());
	}

    @Test
    public void bookController_GetBookByAsinNumber_WhenAnExceptionOccurs_ShouldInternalServerError() throws Exception {

        final Book book = BookStub.get();

        when(bookService.findByAsin(book.getAsin())).thenThrow(new InternalServerErrorException());

        mockMvc.perform(get("/v1/books/" + book.getAsin()))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void bookController_GetAllBooks_ShouldReturnAllBooks() throws Exception {

        final BookResponseV1 book = BookStub.getResponse();

        List<BookResponseV1> bookList = new ArrayList<BookResponseV1>();
		bookList.add(book);

        when(bookService.getAll()).thenReturn(bookList);

        mockMvc.perform(get("/v1/books"))
                .andExpect(jsonPath("$.[0].asin").value(book.getAsin()))
                .andExpect(status().isOk());

    }

	@Test
	public void bookController_GetAllBooks_WhenAnExceptionOccurs_ShouldInternalServerError() throws Exception {
		when(bookService.getAll()).thenThrow(new InternalServerErrorException());

		mockMvc.perform(get("/v1/books"))
				.andExpect(status().isInternalServerError());

	}
    
}
