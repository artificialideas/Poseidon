package com.nnk.springboot.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.service.BidListService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
public class BidListControllerTest {
	private final int ID = 1;
	private BidList bid;

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@MockBean
	private BidListService bidListService;

	@Before
	public void setup() {
		bid = new BidList("Account Test", "Type Test", 10d);
			bidListService.save(bid);
	}

	@Test
	@WithMockUser(username="admin",roles={"USER","ADMIN"})
	@DisplayName("GET - List all bidList REST API //home()")
	public void givenListOfBidLists_whenFindAllBidList_thenReturnBidListsList() throws Exception {
		mockMvc.perform(get("/bidList/list"))
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.size()", is(1)));
	}

	@Test
	@WithMockUser(username="admin",roles={"USER","ADMIN"})
	@DisplayName("POST - Create new bidList REST API //validate()")
	public void givenBidListObject_whenCreateBidList_thenReturnSavedBidList() throws Exception {
		mockMvc.perform(post("/bidList/validate")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(bid)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.account", is(bid.getAccount())))
				.andExpect(jsonPath("$.type", is(bid.getType())))
				.andExpect(jsonPath("$.bidQuantity", is(bid.getBidQuantity())));
	}

	@Test
	@WithMockUser(username="admin",roles={"USER","ADMIN"})
	@DisplayName("POST - Update bidList REST API -> positive scenario //updateBid()")
	public void givenUpdatedBidList_whenUpdateBidList_thenReturnUpdateBidListObject() throws Exception {
		bid.setAccount("Account");
		bid.setType("Type");
		bid.setBidQuantity(20d);

		 mockMvc.perform(post("/bidList/update/" + ID)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(bid)))
				 .andExpect(status().isOk())
				 .andExpect(jsonPath("$.account", is(bid.getAccount())))
				 .andExpect(jsonPath("$.type", is(bid.getType())))
				 .andExpect(jsonPath("$.bidQuantity", is(bid.getBidQuantity())));
	}

	@Test
	@WithMockUser(username="admin",roles={"USER","ADMIN"})
	@DisplayName("POST - Update bidList REST API -> negative scenario //updateBid()")
	public void givenUpdatedBidList_whenUpdateBidList_thenReturn404() throws Exception {
		BidList updatedBid = new BidList("Account 1", "Type 1", 21d);

		given(bidListService.findById(ID)).willReturn(Optional.empty());
			bidListService.save(updatedBid);

		mockMvc.perform(post("/bidList/update/" + ID)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(updatedBid)))
				.andExpect(status().isNotFound())
				.andDo(print());

		bidListService.delete(updatedBid);
	}

	@Test
	@WithMockUser(username="admin",roles={"USER","ADMIN"})
	@DisplayName("GET - Delete bidList REST API //deleteBid()")
	public void givenBidListObject_whenDeleteBidList_thenReturn200() throws Exception {
		mockMvc.perform(get("/bidList/delete/" + bid.getId()))
				.andExpect(status().isOk())
				.andDo(print());
	}
}
