package com.bookrecommendationsystem.recommendation.controller;

import com.bookrecommendationsystem.recommendation.domain.Rating;
import com.bookrecommendationsystem.recommendation.domain.User;
import com.bookrecommendationsystem.recommendation.dto.RatingResponseV1;
import com.bookrecommendationsystem.recommendation.dto.UserResponseV1;
import com.bookrecommendationsystem.recommendation.exception.BookNotFoundException;
import com.bookrecommendationsystem.recommendation.exception.UserNotFoundException;
import com.bookrecommendationsystem.recommendation.service.UserService;
import com.bookrecommendationsystem.recommendation.stub.RatingStub;
import com.bookrecommendationsystem.recommendation.stub.UserStub;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.junit.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTests {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

	@Test
	public void shouldCreateNewUser() throws Exception {
		final UserResponseV1 user = UserStub.getResponse();

		when(userService.createNewUser(any())).thenReturn(user);

		mockMvc.perform(post("/v1/users" )
                .contentType(MediaType.APPLICATION_JSON_UTF8)
				.content("{\"username\": \"leomn138\", \"name\": \"Leonardo\"}"))
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(status().isCreated());
	}

    @Test
    public void shouldReturnBadRequestWhenUserIsNotFilledCorrectlyOnCreateNewUser() throws Exception {
        when(userService.createNewUser(any())).thenThrow(new IllegalArgumentException());
        mockMvc.perform(post("/v1/users" )
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldCreateNewRating() throws Exception {
        final RatingResponseV1 rating = RatingStub.getResponse();

        when(userService.upsertRating(any(), any(), any())).thenReturn(rating);

        mockMvc.perform(post("/v1/users/" + rating.getUsername() + "/ratings" )
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"asin\": \"1234\", \"ratingLevel\": \"LIKED\"}"))
                .andExpect(jsonPath("$.ratingLevel").value(rating.getRatingLevel()))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldReturnBadRequestWhenUserDoesNotExistOnCreateRating() throws Exception {
        final Rating rating = RatingStub.get();

        when(userService.upsertRating(any(), any(), any())).thenThrow(new UserNotFoundException());

        mockMvc.perform(post("/v1/users/" + rating.getUser().getUsername() + "/ratings" )
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"asin\": \"1234\", \"ratingLevel\": \"LIKED\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestWhenBookDoesNotExistOnCreateRating() throws Exception {
        final Rating rating = RatingStub.get();

        when(userService.upsertRating(any(), any(), any())).thenThrow(new BookNotFoundException());

        mockMvc.perform(post("/v1/users/" + rating.getUser().getUsername() + "/ratings" )
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"asin\": \"1234\", \"ratingLevel\": \"LIKED\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestWhenRatingLevelDoesNotExistOnCreateRating() throws Exception {
        final Rating rating = RatingStub.get();

        when(userService.upsertRating(any(), any(), any())).thenThrow(new IllegalArgumentException());

        mockMvc.perform(post("/v1/users/" + rating.getUser().getUsername() + "/ratings" )
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"asin\": \"1234\", \"ratingLevel\": \"LIKED\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldUpdateRating() throws Exception {
        final RatingResponseV1 rating = RatingStub.getResponse();

        when(userService.upsertRating(any(), any())).thenReturn(rating);

        mockMvc.perform(put("/v1/users/" + rating.getUsername() + "/ratings/" + rating.getAsin() )
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("\"ratingLevel\": \"LIKED\"}"))
                .andExpect(jsonPath("$.ratingLevel").value(rating.getRatingLevel()))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnBadRequestWhenUserDoesNotExistOnGetRecommendations() throws Exception {
        final Rating rating = RatingStub.get();

        when(userService.getRecommendations(any())).thenThrow(new UserNotFoundException());

        mockMvc.perform(get("/v1/users/" + rating.getUser().getUsername() + "/recommendations" ))
                .andExpect(status().isBadRequest());
    }
}
