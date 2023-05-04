package com.nnk.springboot.service;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RatingServiceTest {
	@Autowired
	private static RatingRepository ratingRepository;

	@BeforeAll
	public static void setUp() {
		Rating rating1 = new Rating("Moodys Rating 1", "Sand PRating 1", "Fitch Rating 1", 10);
		ratingRepository.save(rating1);
		Rating rating2 = new Rating("Moodys Rating 2", "Sand PRating 2", "Fitch Rating 2", 20);
		ratingRepository.save(rating2);
	}

	@Test
	@DisplayName("Save rating //save()")
	public void givenNewRating_whenCreateRating_thenReturnRatingObject() {
		Rating rating = new Rating("Moodys Rating 3", "Sand PRating 3", "Fitch Rating 3", 30);
			ratingRepository.save(rating);
		Rating savedRating = ratingRepository.findById(rating.getId()).get();

		Assert.assertNotNull(savedRating.getId());
		Assert.assertEquals(30, savedRating.getOrderNumber(), 0.0);
	}

	@Test
	@DisplayName("Update rating //save()")
	public void givenExistentRating_whenUpdateRating_thenReturnRatingObject() {
		int bidId = 0;
		Rating savedRating = ratingRepository.findById(bidId).get();
			savedRating.setOrderNumber(11);
			ratingRepository.save(savedRating);
		Rating updatedRating = ratingRepository.findById(savedRating.getId()).get();

		Assert.assertEquals(11, updatedRating.getOrderNumber(), 0.0);
	}

	@Test
	@DisplayName("Find rating //findAll()")
	public void givenListOfRatings_whenFindAllRating_thenReturnRatingsList() {
		List<Rating> listResult = ratingRepository.findAll();

		Assert.assertTrue(listResult.size() > 0);
	}

	@Test
	@DisplayName("Find rating //findById()")
	public void givenRating_whenFindByIdRating_thenReturnRatingObject() {
		int ratingId = 0;
		Rating rating = ratingRepository.findById(ratingId).get();

		Assert.assertEquals(Optional.ofNullable(rating.getId()), Optional.of(0));
	}

	@Test
	@DisplayName("Delete rating //delete()")
	public void givenRatingObject_whenDeleteRating_thenReturn200() {
		int ratingId = 0;
		Rating savedRating = ratingRepository.findById(ratingId).get();
			ratingRepository.delete(savedRating);
		Optional<Rating> rating = ratingRepository.findById(ratingId);

		Assert.assertFalse(rating.isPresent());
	}
}