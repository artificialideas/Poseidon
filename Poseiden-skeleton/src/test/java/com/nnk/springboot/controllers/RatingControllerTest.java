package com.nnk.springboot.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.service.RatingService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RatingControllerTest {
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

	@After
	public void tearDown() {
		ratingService.delete(rating);
	}

	@Test
	@WithMockUser
	@DisplayName("GET - List all rating REST API //home()")
	public void givenListOfRatings_whenFindAllRating_thenReturnRatingsList() throws Exception {
		mockMvc.perform(get("/rating/list")
						.contentType(MediaType.TEXT_HTML))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
	}

	@Test
	@WithMockUser
	@DisplayName("POST - Create new rating REST API //validate()")
	public void givenRatingObject_whenCreateRating_thenReturnSavedRating() throws Exception {
		mockMvc.perform(post("/rating/validate")
						.param("moodysRating", "Moodys")
						.param("sandPRating", "Sand P")
						.param("fitchRating", "Fitch")
						.param("orderNumber", "20")
						.with(csrf()))
				.andExpect(redirectedUrl("/rating/list"));
	}

	@Test
	@WithMockUser
	@DisplayName("POST - Update rating REST API -> positive scenario //updateRating()")
	public void givenUpdatedRating_whenUpdateRating_thenReturnUpdateRatingObject() throws Exception {
		mockMvc.perform(post("/rating/update/" + rating.getId())
						.param("moodysRating", "Moodys 2")
						.param("sandPRating", "Sand P 2")
						.param("fitchRating", "Fitch 2")
						.param("orderNumber", "30")
						.with(csrf()))
				.andExpect(redirectedUrl("/rating/list"));
	}

	@Test
	@WithMockUser
	@DisplayName("POST - Update rating REST API -> negative scenario //updateRating()")
	public void givenUpdatedRating_whenUpdateRating_thenReturn404() throws Exception {
		mockMvc.perform(post("/rating/update/" + rating.getId())
						.param("moodysRating", "Moodys 2")
						.param("sandPRating", "Sand P 2")
						.param("fitchRating", "Fitch 2")
						.param("orderNumber", "Text")
						.with(csrf()))
				.andExpect(model().hasErrors());
	}

	@Test
	@WithMockUser
	@DisplayName("GET - Delete rating REST API //deleteRating()")
	public void givenRatingObject_whenDeleteRating_thenReturn200() throws Exception {
		Rating newRating = new Rating("Moodys 1", "Sand P 1", "Fitch 1", 21);
			ratingService.save(newRating);

		mockMvc.perform(get("/rating/delete/" + newRating.getId()))
				.andExpect(redirectedUrl("/rating/list"));
	}
}
