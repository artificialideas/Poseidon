package com.nnk.springboot.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.service.TradeService;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class TradeControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TradeService tradeService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@WithMockUser(username="admin",roles={"USER","ADMIN"})
	@DisplayName("GET - List all trades REST API //home()")
	public void givenListOfTrades_whenFindAllTrade_thenReturnTradesList() throws Exception {
		// given - precondition or setup
		List<Trade> listOfTrades = new ArrayList<>();
			listOfTrades.add(new Trade("Trade Account 1", "Type 1", 10d));
			listOfTrades.add(new Trade("Trade Account 2", "Type 2", 20d));
		given(tradeService.findAll()).willReturn(listOfTrades);

		// when -  action or the behaviour that we are going test
		ResultActions response = mockMvc.perform(get("/trade/list"));

		// then - verify the output
		response.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.size()",
						is(listOfTrades.size())));
	}

	@Test
	@WithMockUser(username="admin",roles={"USER","ADMIN"})
	@DisplayName("POST - Create new trade REST API //validate()")
	public void givenTradeObject_whenCreateTrade_thenReturnSavedTrade() throws Exception {
		// given - precondition or setup
		Trade trade = new Trade("Trade Account", "Type", 10d);
		tradeService.save(trade);

		// when - action or behaviour that we are going test
		ResultActions response = mockMvc.perform(post("/trade/validate")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(trade)));

		// then - verify the result or output using assert statements
		response.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.account",
						is(trade.getAccount())))
				.andExpect(jsonPath("$.type",
						is(trade.getType())))
				.andExpect(jsonPath("$.buyQuantity",
						is(trade.getBuyQuantity())));
	}

	@Test
	@WithMockUser(username="admin",roles={"USER","ADMIN"})
	@DisplayName("POST - Update trade REST API -> positive scenario //updateTrade()")
	public void givenUpdatedTrade_whenUpdateTrade_thenReturnUpdateTradeObject() throws Exception {
		// given - precondition or setup
		int tradeId = 1;
		Trade savedTrade = new Trade("Trade Account", "Type", 10d);
		Trade updatedTrade = new Trade("Trade", "Type 2", 20d);

		given(tradeService.findById(tradeId)).willReturn(Optional.of(savedTrade));
		tradeService.save(updatedTrade);

		// when -  action or the behaviour that we are going test
		ResultActions response = mockMvc.perform(post("/trade/update/{id}", tradeId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedTrade)));

		// then - verify the output
		response.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.account",
						is(updatedTrade.getAccount())))
				.andExpect(jsonPath("$.type",
						is(updatedTrade.getType())))
				.andExpect(jsonPath("$.buyQuantity",
						is(updatedTrade.getBuyQuantity())));
	}

	@Test
	@WithMockUser(username="admin",roles={"USER","ADMIN"})
	@DisplayName("POST - Update trade REST API -> negative scenario //updateTrade()")
	public void givenUpdatedTrade_whenUpdateTrade_thenReturn404() throws Exception {
		// given - precondition or setup
		int tradeId = 1;
		Trade savedTrade = new Trade("Trade Account", "Type", 10d);
		Trade updatedTrade = new Trade("Trade", "Type 2", 20d);

		given(tradeService.findById(tradeId)).willReturn(Optional.empty());
		tradeService.save(updatedTrade);

		// when -  action or the behaviour that we are going test
		ResultActions response = mockMvc.perform(post("/trade/update/{id}", tradeId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedTrade)));

		// then - verify the output
		response.andExpect(status().isNotFound())
				.andDo(print());
	}

	@Test
	@WithMockUser(username="admin",roles={"USER","ADMIN"})
	@DisplayName("GET - Delete trade REST API //deleteTrade()")
	public void givenTradeObject_whenDeleteTrade_thenReturn200() throws Exception {
		// given - precondition or setup
		Trade trade = new Trade("Trade Account", "Type", 10d);
		willDoNothing().given(tradeService).delete(trade);

		// when -  action or the behaviour that we are going test
		ResultActions response = mockMvc.perform(get("/trade/delete/{id}", trade.getId()));

		// then - verify the output
		response.andExpect(status().isOk())
				.andDo(print());
	}
}
