package com.nnk.springboot.service;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class BidListServiceImplTest {
	@Autowired
	private static BidListRepository bidListRepository;

	/*@BeforeEach
	public void setUp() {
		BidList bid1 = new BidList("Account Test 1", "Type Test 1", 10d);
			bidListRepository.save(bid1);
		BidList bid2 = new BidList("Account Test 2", "Type Test 2", 20d);
			bidListRepository.save(bid2);
	}*/

	@Test
	@DisplayName("Save bidList //save()")
	public void givenNewBidList_whenCreateBidList_thenReturnBidListObject() {
		BidList bid = new BidList("Account Test 3", "Type Test 3", 30d);
		bidListRepository.save(bid);
		BidList savedBid = bidListRepository.findById(bid.getId()).get();

		assertNotNull(savedBid.getId());
		assertEquals(30d, savedBid.getBidQuantity(), 0.0);
	}

	@Test
	@DisplayName("Update bidList //save()")
	public void givenExistentBidList_whenUpdateBidList_thenReturnBidListObject() {
		int bidId = 0;
		BidList savedBid = bidListRepository.getOne(bidId);
		savedBid.setBidQuantity(11d);
		bidListRepository.saveAndFlush(savedBid);
		BidList updatedBid = bidListRepository.getOne(savedBid.getId());

		assertEquals(11d, updatedBid.getBidQuantity(), 0.0);
	}

	@Test
	@DisplayName("Find bidList //findAll()")
	public void givenListOfBidLists_whenFindAllBidList_thenReturnBidListsList() {
		List<BidList> listResult = bidListRepository.findAll();

		assertTrue(listResult.size() > 0);
	}

	@Test
	@DisplayName("Find bidList //findById()")
	public void givenBidList_whenFindByIdBidList_thenReturnBidListObject() {
		int bidId = 0;
		BidList bid = bidListRepository.getOne(bidId);

		assertEquals(Optional.ofNullable(bid.getId()), Optional.of(0));
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

		assertFalse(bidListRepository.findAll().contains(savedBid));
	}
}
