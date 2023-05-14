package com.nnk.springboot.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.service.TradeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
public class TradeControllerTest {
	private final int ID = 1;
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

	@Test
	@WithMockUser(username="admin",roles={"USER","ADMIN"})
	@DisplayName("GET - List all trades REST API //home()")
	public void givenListOfTrades_whenFindAllTrade_thenReturnTradesList() throws Exception {
		mockMvc.perform(get("/trade/list"))
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.size()", is(1)));
	}

	@Test
	@WithMockUser(username="admin",roles={"USER","ADMIN"})
	@DisplayName("POST - Create new trade REST API //validate()")
	public void givenTradeObject_whenCreateTrade_thenReturnSavedTrade() throws Exception {
		mockMvc.perform(post("/trade/validate")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(trade)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.account", is(trade.getAccount())))
				.andExpect(jsonPath("$.type", is(trade.getType())))
				.andExpect(jsonPath("$.buyQuantity", is(trade.getBuyQuantity())));
	}

	@Test
	@WithMockUser(username="admin",roles={"USER","ADMIN"})
	@DisplayName("POST - Update trade REST API -> positive scenario //updateTrade()")
	public void givenUpdatedTrade_whenUpdateTrade_thenReturnUpdateTradeObject() throws Exception {
		trade.setAccount("Trade 1");
		trade.setType("Type 1");
		trade.setBuyQuantity(11d);

		mockMvc.perform(post("/trade/update/" + ID)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(trade)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.account", is(trade.getAccount())))
				.andExpect(jsonPath("$.type", is(trade.getType())))
				.andExpect(jsonPath("$.buyQuantity", is(trade.getBuyQuantity())));
	}

	@Test
	@WithMockUser(username="admin",roles={"USER","ADMIN"})
	@DisplayName("POST - Update trade REST API -> negative scenario //updateTrade()")
	public void givenUpdatedTrade_whenUpdateTrade_thenReturn404() throws Exception {
		Trade updatedTrade = new Trade("Trade 2", "Type 2", 20d);

		given(tradeService.findById(ID)).willReturn(Optional.empty());
			tradeService.save(updatedTrade);

		mockMvc.perform(post("/trade/update/" + ID)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(updatedTrade)))
				.andExpect(status().isNotFound())
				.andDo(print());

		tradeService.delete(updatedTrade);
	}

	@Test
	@WithMockUser(username="admin",roles={"USER","ADMIN"})
	@DisplayName("GET - Delete trade REST API //deleteTrade()")
	public void givenTradeObject_whenDeleteTrade_thenReturn200() throws Exception {
		mockMvc.perform(get("/trade/delete/{id}", trade.getId()))
				.andExpect(status().isOk())
				.andDo(print());
	}
}
