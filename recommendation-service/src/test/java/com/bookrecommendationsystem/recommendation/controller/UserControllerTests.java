package com.bookrecommendationsystem.recommendation.controller;

import com.bookrecommendationsystem.recommendation.domain.UserBookRating;
import com.bookrecommendationsystem.recommendation.dto.BookResponseV1;
import com.bookrecommendationsystem.recommendation.dto.RatingResponseV1;
import com.bookrecommendationsystem.recommendation.dto.RecommendationResponseV1;
import com.bookrecommendationsystem.recommendation.dto.UserResponseV1;
import com.bookrecommendationsystem.recommendation.service.UserService;
import com.bookrecommendationsystem.recommendation.stub.RatingStub;
import com.bookrecommendationsystem.recommendation.stub.RecommendationStub;
import com.bookrecommendationsystem.recommendation.stub.UserStub;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.junit.Test;
import javax.ws.rs.InternalServerErrorException;
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
	public void userController_CreateNewUser_ShouldReturnCorrectUserResponse() throws Exception {
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
    public void userController_CreateNewUser_WhenAnExceptionOccurs_ShouldReturnInternalServerErrorResponse() throws Exception {
        when(userService.createNewUser(any())).thenThrow(new InternalServerErrorException());

        mockMvc.perform(post("/v1/users" )
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"username\": \"leomn138\", \"name\": \"Leonardo\"}"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void userController_CreateNewRating_ShouldReturnCorrectRatingResponse() throws Exception {
        final RatingResponseV1 rating = RatingStub.getResponse();

        when(userService.createNewRating(any(), any())).thenReturn(rating);

        mockMvc.perform(post("/v1/users/" + rating.getUsername() + "/ratings" )
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"asin\": \"1234\", \"ratingLevel\": \"LIKED\"}"))
                .andExpect(jsonPath("$.username").value(rating.getUsername()))
                .andExpect(status().isCreated());
    }

    @Test
    public void userController_CreateNewRating_WhenAnExceptionOccurs_ShouldReturnInternalServerErrorResponse() throws Exception {
        final UserBookRating rating = RatingStub.get();

        when(userService.updateRating(any(), any(), any())).thenThrow(new InternalServerErrorException());

        mockMvc.perform(post("/v1/users/" + rating.getUser().getUsername() + "/ratings" )
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"asin\": \"1234\", \"ratingLevel\": \"LIKED\"}"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void userController_UpdateRating_ShouldReturnCorrectRatingResponse() throws Exception {
        final RatingResponseV1 rating = RatingStub.getResponse();

        when(userService.updateRating(any(), any(), any())).thenReturn(rating);

        mockMvc.perform(put("/v1/users/" + rating.getUsername() + "/ratings/" + rating.getAsin() )
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"ratingLevel\": \"LIKED\"}"))
                .andExpect(jsonPath("$.ratingLevel").value(rating.getRatingLevel()))
                .andExpect(status().isCreated());
    }

    @Test
    public void userController_UpdateRating_WhenAnExceptionOccurs_ShouldReturnInternalServerErrorResponse() throws Exception {
        final UserBookRating rating = RatingStub.get();

        when(userService.updateRating(any(), any(), any())).thenThrow(new InternalServerErrorException());

        mockMvc.perform(post("/v1/users/" + rating.getUser().getUsername() + "/ratings" )
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"asin\": \"1234\", \"ratingLevel\": \"LIKED\"}"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void userController_GetRecommendations_ShouldReturnCorrectRecommendations() throws Exception {

        final RecommendationResponseV1 recommendationResponse = RecommendationStub.getResponse();

        when(userService.getRecommendations(any())).thenReturn(recommendationResponse);

        mockMvc.perform(get("/v1/users/leomn138/recommendations"))
                .andExpect(jsonPath("$.books.[0].asin").value(((BookResponseV1)recommendationResponse.getBooks().toArray()[0]).getAsin()))
                .andExpect(status().isOk());

    }

    @Test
    public void userController_GetRecommendations_WhenAnExceptionOccurs_ShouldReturnInternalServerErrorResponse() throws Exception {
        final UserBookRating rating = RatingStub.get();

        when(userService.getRecommendations(any())).thenThrow(new InternalServerErrorException());

        mockMvc.perform(get("/v1/users/leomn138/recommendations" ))
                .andExpect(status().isInternalServerError());
    }
}
