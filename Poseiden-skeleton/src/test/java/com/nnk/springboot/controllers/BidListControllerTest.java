package com.nnk.springboot.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.service.BidListService;
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
public class BidListControllerTest {
	private BidList bid;

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private BidListService bidListService;

	@Before
	public void setup() {
		bid = new BidList("Account Test", "Type Test", 10d);
		bidListService.save(bid);
	}

	@After
	public void tearDown() {
		if (bidListService.findById(bid.getId()).isPresent()) {
			bidListService.delete(bid);
		}
	}

	@Test
	@WithMockUser
	@DisplayName("GET - List all bidList REST API -> positive scenario //home()")
	public void givenListOfBidLists_whenFindAllBidList_thenReturnBidListsList() throws Exception {
		mockMvc.perform(get("/bidList/list")
						.contentType(MediaType.TEXT_HTML))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
	}

	@Test
	@DisplayName("GET - List all bidList REST API -> negative scenario //home()")
	public void givenListOfBidLists_whenFindAllBidListWithoutAutorization_thenRedirect() throws Exception {
		mockMvc.perform(get("/bidList/list")
						.contentType(MediaType.TEXT_HTML))
				.andExpect(redirectedUrl("http://localhost/login"));
	}

	@Test
	@WithMockUser
	@DisplayName("POST - Create new bidList REST API //validate()")
	public void givenBidListObject_whenCreateBidList_thenReturnSavedBidList() throws Exception {
		mockMvc.perform(post("/bidList/validate")
						.param("account", "Account Test")
						.param("type", "Type Test")
						.param("bidQuantity", "10d")
						.with(csrf()))
				.andExpect(redirectedUrl("/bidList/list"));
	}

	@Test
	@WithMockUser
	@DisplayName("POST - Update bidList REST API -> positive scenario //updateBid()")
	public void givenUpdatedBidList_whenUpdateBidList_thenReturnUpdateBidListObject() throws Exception {
 		mockMvc.perform(post("/bidList/update/" + bid.getId())
						 .param("account", "Account")
						 .param("type", "Type")
						 .param("bidQuantity", "20d")
						 .with(csrf()))
				.andExpect(redirectedUrl("/bidList/list"));
	}

	@Test
	@WithMockUser
	@DisplayName("POST - Update bidList REST API -> negative scenario //updateBid()")
	public void givenUpdatedBidList_whenUpdateBidList_thenReturn404() throws Exception {
		mockMvc.perform(post("/bidList/update/" + bid.getId())
						.param("account", "Account")
						.param("type", "Type")
						.param("bidQuantity", "20L")
						.with(csrf()))
				.andExpect(model().hasErrors());
	}

	@Test
	@WithMockUser
	@DisplayName("GET - Delete bidList REST API //deleteBid()")
	public void givenBidListObject_whenDeleteBidList_thenReturn200() throws Exception {
		BidList newBid = new BidList("Account Test 2", "Type Test 2", 20d);
			bidListService.save(newBid);

		mockMvc.perform(get("/bidList/delete/" + newBid.getId()))
				.andExpect(redirectedUrl("/bidList/list"));
	}
}
