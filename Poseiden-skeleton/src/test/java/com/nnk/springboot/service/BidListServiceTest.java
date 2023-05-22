package com.nnk.springboot.service;

import com.nnk.springboot.domain.BidList;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
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
public class BidListServiceTest {
	@Autowired
	private BidListService bidListService;

	@Test
	@Order(1)
	@DisplayName("Save bidList //save()")
	public void givenNewBidList_whenCreateBidList_thenReturnBidListObject() {
		BidList bid = new BidList("Account Test 1", "Type Test 1", 10d);
			bidListService.save(bid);
		BidList savedBid = bidListService.findById(bid.getId()).get();

		assertNotNull(savedBid.getId());
		assertEquals(10d, savedBid.getBidQuantity(), 0.0);
	}

	@Test
	@Order(2)
	@DisplayName("Find bidList //findAll()")
	public void givenListOfBidLists_whenFindAllBidList_thenReturnBidListsList() {
		List<BidList> listResult = bidListService.findAll();

		assertTrue(listResult.size() > 0);
	}

	@Test
	@Order(3)
	@DisplayName("Find bidList //findById()")
	public void givenBidList_whenFindByIdBidList_thenReturnBidListObject() {
		int bidId = 1;
		BidList bid = bidListService.findById(bidId).get();

		assertEquals(Optional.ofNullable(bid.getId()), Optional.of(1));
	}

	@Test
	@Order(4)
	@DisplayName("Update bidList //save()")
	public void givenExistentBidList_whenUpdateBidList_thenReturnBidListObject() {
		int bidId = 1;
		BidList savedBid = bidListService.findById(bidId).get();
			savedBid.setBidQuantity(11d);
			bidListService.save(savedBid);
		BidList updatedBid = bidListService.findById(savedBid.getId()).get();

		assertEquals(11d, updatedBid.getBidQuantity(), 0.0);
	}

	@Test
	@Order(5)
	@DisplayName("Delete bidList //delete()")
	public void givenBidListObject_whenDeleteBidList_thenReturn200() {
		int bidId = 1;
		BidList savedBid = bidListService.findById(bidId).get();
			bidListService.delete(savedBid);
		Optional<BidList> bidList = bidListService.findById(bidId);

		assertFalse(bidList.isPresent());
	}
}
