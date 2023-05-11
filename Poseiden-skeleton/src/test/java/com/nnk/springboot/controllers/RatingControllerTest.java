package com.nnk.springboot.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.service.RatingService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RatingControllerTest {
	private final int ID = 1;
	private Rating rating;

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private RatingService ratingService;

	@Before
	public void setup() {
		rating = new Rating("Moodys Rating", "Sand PRating", "Fitch Rating", 10);
			ratingService.save(rating);
	}

	@Test
	@WithMockUser(username="admin",roles={"USER","ADMIN"})
	@DisplayName("GET - List all rating REST API //home()")
	public void givenListOfRatings_whenFindAllRating_thenReturnRatingsList() throws Exception {
		mockMvc.perform(get("/rating/list"))
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.size()", is(1)));
	}

	@Test
	@WithMockUser(username="admin",roles={"USER","ADMIN"})
	@DisplayName("POST - Create new rating REST API //validate()")
	public void givenRatingObject_whenCreateRating_thenReturnSavedRating() throws Exception {
		mockMvc.perform(post("/rating/validate")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(rating)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.moodysRating", is(rating.getMoodysRating())))
				.andExpect(jsonPath("$.sandPRating", is(rating.getSandPRating())))
				.andExpect(jsonPath("$.fitchRating", is(rating.getFitchRating())))
				.andExpect(jsonPath("$.orderNumber", is(rating.getOrderNumber())));
	}

	@Test
	@WithMockUser(username="admin",roles={"USER","ADMIN"})
	@DisplayName("POST - Update rating REST API -> positive scenario //updateRating()")
	public void givenUpdatedRating_whenUpdateRating_thenReturnUpdateRatingObject() throws Exception {
		rating.setMoodysRating("Moodys");
		rating.setSandPRating("Sand P");
		rating.setFitchRating("Fitch");
		rating.setOrderNumber(20);

		mockMvc.perform(post("/rating/update/" + ID)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(rating)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.moodysRating", is(rating.getMoodysRating())))
				.andExpect(jsonPath("$.sandPRating", is(rating.getSandPRating())))
				.andExpect(jsonPath("$.fitchRating", is(rating.getFitchRating())))
				.andExpect(jsonPath("$.orderNumber", is(rating.getOrderNumber())));
	}

	@Test
	@WithMockUser(username="admin",roles={"USER","ADMIN"})
	@DisplayName("POST - Update rating REST API -> negative scenario //updateRating()")
	public void givenUpdatedRating_whenUpdateRating_thenReturn404() throws Exception {
		Rating updatedRating = new Rating("Moodys 1", "Sand P 1", "Fitch 1", 21);

		given(ratingService.findById(ID)).willReturn(Optional.empty());
			ratingService.save(updatedRating);

		mockMvc.perform(post("/rating/update/" + ID)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(updatedRating)))
				.andExpect(status().isNotFound())
				.andDo(print());

		ratingService.delete(updatedRating);
	}

	@Test
	@WithMockUser(username="admin",roles={"USER","ADMIN"})
	@DisplayName("GET - Delete rating REST API //deleteRating()")
	public void givenRatingObject_whenDeleteRating_thenReturn200() throws Exception {
		mockMvc.perform(get("/rating/delete/" + rating.getId()))
				.andExpect(status().isOk())
				.andDo(print());
	}
}
