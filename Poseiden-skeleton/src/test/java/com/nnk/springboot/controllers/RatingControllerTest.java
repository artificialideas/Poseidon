package com.nnk.springboot.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.service.RatingService;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class RatingControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private RatingService ratingService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@WithMockUser(username="admin",roles={"USER","ADMIN"})
	@DisplayName("GET - List all rating REST API //home()")
	public void givenListOfRatings_whenFindAllRating_thenReturnRatingsList() throws Exception {
		// given - precondition or setup
		List<Rating> listOfRatings = new ArrayList<>();
			listOfRatings.add(new Rating("Moodys Rating 1", "Sand PRating 1", "Fitch Rating 1", 10));
			listOfRatings.add(new Rating("Moodys Rating 2", "Sand PRating 2", "Fitch Rating 2", 20));
		given(ratingService.findAll()).willReturn(listOfRatings);

		// when -  action or the behaviour that we are going test
		ResultActions response = mockMvc.perform(get("/rating/list"));

		// then - verify the output
		response.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.size()",
						is(listOfRatings.size())));
	}

	@Test
	@WithMockUser(username="admin",roles={"USER","ADMIN"})
	@DisplayName("POST - Create new rating REST API //validate()")
	public void givenRatingObject_whenCreateRating_thenReturnSavedRating() throws Exception {
		// given - precondition or setup
		Rating rating = new Rating("Moodys Rating", "Sand PRating", "Fitch Rating", 10);
		ratingService.save(rating);

		// when - action or behaviour that we are going test
		ResultActions response = mockMvc.perform(post("/rating/validate")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(rating)));

		// then - verify the result or output using assert statements
		response.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.moodysRating",
						is(rating.getMoodysRating())))
				.andExpect(jsonPath("$.sandPRating",
						is(rating.getSandPRating())))
				.andExpect(jsonPath("$.fitchRating",
						is(rating.getFitchRating())))
				.andExpect(jsonPath("$.orderNumber",
						is(rating.getOrderNumber())));
	}

	@Test
	@WithMockUser(username="admin",roles={"USER","ADMIN"})
	@DisplayName("POST - Update rating REST API -> positive scenario //updateRating()")
	public void givenUpdatedRating_whenUpdateRating_thenReturnUpdateRatingObject() throws Exception {
		// given - precondition or setup
		int ratingId = 1;
		Rating savedRating = new Rating("Moodys Rating", "Sand PRating", "Fitch Rating", 10);
		Rating updatedRating = new Rating("Moodys", "Sand P", "Fitch", 20);

		given(ratingService.findById(ratingId)).willReturn(Optional.of(savedRating));
		ratingService.save(updatedRating);

		// when -  action or the behaviour that we are going test
		ResultActions response = mockMvc.perform(post("/rating/update/{id}", ratingId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedRating)));

		// then - verify the output
		response.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.moodysRating",
						is(updatedRating.getMoodysRating())))
				.andExpect(jsonPath("$.sandPRating",
						is(updatedRating.getSandPRating())))
				.andExpect(jsonPath("$.fitchRating",
						is(updatedRating.getFitchRating())))
				.andExpect(jsonPath("$.orderNumber",
						is(updatedRating.getOrderNumber())));
	}

	@Test
	@WithMockUser(username="admin",roles={"USER","ADMIN"})
	@DisplayName("POST - Update rating REST API -> negative scenario //updateRating()")
	public void givenUpdatedRating_whenUpdateRating_thenReturn404() throws Exception {
		// given - precondition or setup
		int ratingId = 1;
		Rating savedRating = new Rating("Moodys Rating", "Sand PRating", "Fitch Rating", 10);
		Rating updatedRating = new Rating("Moodys", "Sand P", "Fitch", 20);

		given(ratingService.findById(ratingId)).willReturn(Optional.empty());
		ratingService.save(updatedRating);

		// when -  action or the behaviour that we are going test
		ResultActions response = mockMvc.perform(post("/rating/update/{id}", ratingId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedRating)));

		// then - verify the output
		response.andExpect(status().isNotFound())
				.andDo(print());
	}

	@Test
	@WithMockUser(username="admin",roles={"USER","ADMIN"})
	@DisplayName("GET - Delete rating REST API //deleteRating()")
	public void givenRatingObject_whenDeleteRating_thenReturn200() throws Exception {
		// given - precondition or setup
		Rating rating = new Rating("Moodys Rating", "Sand PRating", "Fitch Rating", 10);
		willDoNothing().given(ratingService).delete(rating);

		// when -  action or the behaviour that we are going test
		ResultActions response = mockMvc.perform(get("/rating/delete/{id}", rating.getId()));

		// then - verify the output
		response.andExpect(status().isOk())
				.andDo(print());
	}
}
