package com.nnk.springboot.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.service.TradeService;
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
public class TradeControllerTest {
	private Trade trade;

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private TradeService tradeService;

	@Before
	public void setup() {
		trade = new Trade("Trade Account", "Type", 10d);
			tradeService.save(trade);
	}

	@After
	public void tearDown() {
		tradeService.delete(trade);
	}

	@Test
	@WithMockUser
	@DisplayName("GET - List all trades REST API //home()")
	public void givenListOfTrades_whenFindAllTrade_thenReturnTradesList() throws Exception {
		mockMvc.perform(get("/trade/list")
						.contentType(MediaType.TEXT_HTML))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
	}

	@Test
	@WithMockUser
	@DisplayName("POST - Create new trade REST API //validate()")
	public void givenTradeObject_whenCreateTrade_thenReturnSavedTrade() throws Exception {
		mockMvc.perform(post("/trade/validate")
						.param("account", "Account Test")
						.param("type", "Type Test")
						.param("buyQuantity", "20d")
						.with(csrf()))
				.andExpect(redirectedUrl("/trade/list"));
	}

	@Test
	@WithMockUser
	@DisplayName("POST - Update trade REST API -> positive scenario //updateTrade()")
	public void givenUpdatedTrade_whenUpdateTrade_thenReturnUpdateTradeObject() throws Exception {
		mockMvc.perform(post("/trade/update/" + trade.getId())
						.param("account", "Account Test")
						.param("type", "Type Test")
						.param("buyQuantity", "20d")
						.with(csrf()))
				.andExpect(redirectedUrl("/trade/list"));
	}

	@Test
	@WithMockUser
	@DisplayName("POST - Update trade REST API -> negative scenario //updateTrade()")
	public void givenUpdatedTrade_whenUpdateTrade_thenReturn404() throws Exception {
		mockMvc.perform(post("/trade/update/" + trade.getId())
						.param("account", "Account")
						.param("type", "Type")
						.param("buyQuantity", "20L")
						.with(csrf()))
				.andExpect(model().hasErrors());
	}

	@Test
	@WithMockUser
	@DisplayName("GET - Delete trade REST API //deleteTrade()")
	public void givenTradeObject_whenDeleteTrade_thenReturn200() throws Exception {
		Trade newTrade = new Trade("Trade 2", "Type 2", 20d);
			tradeService.save(newTrade);

		mockMvc.perform(get("/trade/delete/{id}", newTrade.getId()))
				.andExpect(redirectedUrl("/trade/list"));
	}
}
