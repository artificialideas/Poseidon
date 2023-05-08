package com.nnk.springboot.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.service.BidListService;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BidListControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@MockBean
	private BidListService bidListService;

	@Test
	@WithMockUser(username="admin",roles={"USER","ADMIN"})
	@DisplayName("GET - List all bidList REST API //home()")
	public void givenListOfBidLists_whenFindAllBidList_thenReturnBidListsList() throws Exception {
		// given - precondition or setup
		List<BidList> listOfBids = new ArrayList<>();
			listOfBids.add(new BidList("Account Test 1", "Type Test 1", 10d));
			listOfBids.add(new BidList("Account Test 2", "Type Test 2", 20d));
		//bidListService = new BidListServiceImpl();
		bidListService.save(listOfBids.get(0));
		bidListService.save(listOfBids.get(1));

		given(bidListService.findAll()).willReturn(listOfBids);

		// when -  action or the behaviour that we are going test
		ResultActions response = mockMvc.perform(get("/bidList/list"));

		// then - verify the output
		response.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.size()",
						is(listOfBids.size())));
	}

	@Test
	@WithMockUser(username="admin",roles={"USER","ADMIN"})
	@DisplayName("POST - Create new bidList REST API //validate()")
	public void givenBidListObject_whenCreateBidList_thenReturnSavedBidList() throws Exception {
		// given - precondition or setup
		BidList bid = new BidList("Account Test", "Type Test", 10d);
		bidListService.save(bid);

		// when - action or behaviour that we are going test
		ResultActions response = mockMvc.perform(post("/bidList/validate")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(bid)));

		// then - verify the result or output using assert statements
		response.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.account",
						is(bid.getAccount())))
				.andExpect(jsonPath("$.type",
						is(bid.getType())))
				.andExpect(jsonPath("$.bidQuantity",
						is(bid.getBidQuantity())));
	}

	@Test
	@WithMockUser(username="admin",roles={"USER","ADMIN"})
	@DisplayName("POST - Update bidList REST API -> positive scenario //updateBid()")
	public void givenUpdatedBidList_whenUpdateBidList_thenReturnUpdateBidListObject() throws Exception {
		// given - precondition or setup
		int bidId = 1;
		BidList savedBid = new BidList("Account Test", "Type Test", 10d);
		BidList updatedBid = new BidList("Account", "Type", 20d);
		//bidListService = new BidListServiceImpl();

		given(bidListService.findById(bidId)).willReturn(savedBid);
		bidListService.save(updatedBid);

		// when -  action or the behaviour that we are going test
		ResultActions response = mockMvc.perform(post("/bidList/update/{id}", bidId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedBid)));

		// then - verify the output
		response.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.account",
						is(updatedBid.getAccount())))
				.andExpect(jsonPath("$.type",
						is(updatedBid.getType())))
				.andExpect(jsonPath("$.bidQuantity",
						is(updatedBid.getBidQuantity())));
	}

	@Test
	@WithMockUser(username="admin",roles={"USER","ADMIN"})
	@DisplayName("POST - Update bidList REST API -> negative scenario //updateBid()")
	public void givenUpdatedBidList_whenUpdateBidList_thenReturn404() throws Exception {
		// given - precondition or setup
		int bidId = 1;
		BidList savedBid = new BidList("Account Test", "Type Test", 10d);
		BidList updatedBid = new BidList("Account", "Type", 20d);

		//given(bidListService.findById(bidId)).then();
		bidListService.save(updatedBid);

		// when -  action or the behaviour that we are going test
		ResultActions response = mockMvc.perform(post("/bidList/update/{id}", bidId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedBid)));

		// then - verify the output
		response.andExpect(status().isNotFound())
				.andDo(print());
	}

	@Test
	@WithMockUser(username="admin",roles={"USER","ADMIN"})
	@DisplayName("GET - Delete bidList REST API //deleteBid()")
	public void givenBidListObject_whenDeleteBidList_thenReturn200() throws Exception {
		// given - precondition or setup
		BidList bid = new BidList("Account Test", "Type Test", 10d);
		List<BidList> bidIds = new ArrayList<>();
			bidIds.add(bid);
		willDoNothing().given(bidListService).delete(bidIds);

		// when -  action or the behaviour that we are going test
		ResultActions response = mockMvc.perform(get("/bidList/delete/{id}", bid.getId()));

		// then - verify the output
		response.andExpect(status().isOk())
				.andDo(print());
	}
}
