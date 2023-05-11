package com.nnk.springboot.service;

import com.nnk.springboot.domain.Rating;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RatingServiceTest {
	@Autowired
	private RatingService ratingService;

	@Test
	@Order(1)
	@DisplayName("Save rating //save()")
	public void givenNewRating_whenCreateRating_thenReturnRatingObject() {
		Rating rating = new Rating("Moodys Rating 1", "Sand PRating 1", "Fitch Rating 1", 10);
			ratingService.save(rating);
		Rating savedRating = ratingService.findById(rating.getId()).get();

		assertNotNull(savedRating.getId());
		assertEquals(10, savedRating.getOrderNumber(), 0.0);
	}

	@Test
	@Order(2)
	@DisplayName("Find rating //findAll()")
	public void givenListOfRatings_whenFindAllRating_thenReturnRatingsList() {
		List<Rating> listResult = ratingService.findAll();

		assertTrue(listResult.size() > 0);
	}

	@Test
	@Order(3)
	@DisplayName("Find rating //findById()")
	public void givenRating_whenFindByIdRating_thenReturnRatingObject() {
		int ratingId = 1;
		Rating rating = ratingService.findById(ratingId).get();

		assertEquals(Optional.ofNullable(rating.getId()), Optional.of(1));
	}

	@Test
	@Order(4)
	@DisplayName("Update rating //save()")
	public void givenExistentRating_whenUpdateRating_thenReturnRatingObject() {
		int ratingId = 1;
		Rating savedRating = ratingService.findById(ratingId).get();
			savedRating.setOrderNumber(11);
			ratingService.save(savedRating);
		Rating updatedRating = ratingService.findById(savedRating.getId()).get();

		assertEquals(11, updatedRating.getOrderNumber(), 0.0);
	}

	@Test
	@Order(5)
	@DisplayName("Delete rating //delete()")
	public void givenRatingObject_whenDeleteRating_thenReturn200() {
		int ratingId = 1;
		Rating savedRating = ratingService.findById(ratingId).get();
			ratingService.delete(savedRating);
		Optional<Rating> rating = ratingService.findById(ratingId);

		assertFalse(rating.isPresent());
	}
}