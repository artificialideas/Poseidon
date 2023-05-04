package com.nnk.springboot.service;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BidListServiceTest {
	@Autowired
	private static BidListRepository bidListRepository;

	@BeforeAll
	public static void setUp() {
		BidList bid1 = new BidList("Account Test 1", "Type Test 1", 10d);
			bidListRepository.saveAndFlush(bid1);
		BidList bid2 = new BidList("Account Test 2", "Type Test 2", 20d);
			bidListRepository.saveAndFlush(bid2);
	}

	@Test
	@DisplayName("Save bidList //save()")
	public void givenNewBidList_whenCreateBidList_thenReturnBidListObject() {
		BidList bid = new BidList("Account Test 3", "Type Test 3", 30d);
			bidListRepository.saveAndFlush(bid);
		BidList savedBid = bidListRepository.getOne(bid.getId());

		Assert.assertNotNull(savedBid.getId());
		Assert.assertEquals(30d, savedBid.getBidQuantity(), 0.0);
	}

	@Test
	@DisplayName("Update bidList //save()")
	public void givenExistentBidList_whenUpdateBidList_thenReturnBidListObject() {
		int bidId = 0;
		BidList savedBid = bidListRepository.getOne(bidId);
			savedBid.setBidQuantity(11d);
			bidListRepository.saveAndFlush(savedBid);
		BidList updatedBid = bidListRepository.getOne(savedBid.getId());

		Assert.assertEquals(11d, updatedBid.getBidQuantity(), 0.0);
	}

	@Test
	@DisplayName("Find bidList //findAll()")
	public void givenListOfBidLists_whenFindAllBidList_thenReturnBidListsList() {
		List<BidList> listResult = bidListRepository.findAll();

		Assert.assertTrue(listResult.size() > 0);
	}

	@Test
	@DisplayName("Find bidList //findById()")
	public void givenBidList_whenFindByIdBidList_thenReturnBidListObject() {
		int bidId = 0;
		BidList bid = bidListRepository.getOne(bidId);

		Assert.assertEquals(Optional.ofNullable(bid.getId()), Optional.of(0));
	}

	@Test
	@DisplayName("Delete bidList //delete()")
	public void givenBidListObject_whenDeleteBidList_thenReturn200() {
		int bidId = 0;
		BidList savedBid = bidListRepository.getOne(bidId);
		List<BidList> bidIds = new ArrayList<>();
			bidIds.add(savedBid);
			bidListRepository.deleteInBatch(bidIds);
		BidList bidList = bidListRepository.getOne(bidId);

		Assert.assertFalse(bidListRepository.findAll().contains(savedBid));
	}
}
