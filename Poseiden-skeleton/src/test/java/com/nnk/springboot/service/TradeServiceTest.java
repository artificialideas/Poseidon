package com.nnk.springboot.service;

import com.nnk.springboot.domain.Trade;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
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

@RunWith(SpringRunner.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TradeServiceTest {
	@Autowired
	private TradeService tradeService;

	private Trade trade;

	@Before
	public void setUp() {
		trade = new Trade("Trade Account 1", "Type 1", 10d);
		tradeService.save(trade);
	}

	@After
	public void tearDown() {
		if (tradeService.findById(trade.getId()).isPresent()) {
			tradeService.delete(trade);
		}
	}

	@Test
	@Order(1)
	@DisplayName("Save trade //save()")
	public void givenNewTrade_whenCreateTrade_thenReturnTradeObject() {
		Trade savedTrade = tradeService.findById(trade.getId()).get();

		Assert.assertNotNull(savedTrade.getId());
		Assert.assertEquals(10d, savedTrade.getBuyQuantity(), 0.0);
	}

	@Test
	@Order(2)
	@DisplayName("Find trade //findAll()")
	public void givenListOfTrades_whenFindAllTrade_thenReturnTradesList() {
		List<Trade> listResult = tradeService.findAll();

		Assert.assertTrue(listResult.size() > 0);
	}

	@Test
	@Order(3)
	@DisplayName("Find trade //findById()")
	public void givenTrade_whenFindByIdTrade_thenReturnTradeObject() {
		int tradeId = trade.getId();
		Trade trade = tradeService.findById(tradeId).get();

		Assert.assertEquals(Optional.ofNullable(trade.getId()), Optional.of(tradeId));
	}

	@Test
	@Order(4)
	@DisplayName("Update trade //save()")
	public void givenExistentTrade_whenUpdateTrade_thenReturnTradeObject() {
		int tradeId = trade.getId();
		Trade savedTrade = tradeService.findById(tradeId).get();
			savedTrade.setBuyQuantity(11d);
			tradeService.save(savedTrade);
		Trade updatedTrade = tradeService.findById(savedTrade.getId()).get();

		Assert.assertEquals(11d, updatedTrade.getBuyQuantity(), 0.0);
	}

	@Test
	@Order(5)
	@DisplayName("Delete trade //delete()")
	public void givenTradeObject_whenDeleteTrade_thenReturn200() {
		int tradeId = trade.getId();
		Trade savedTrade = tradeService.findById(tradeId).get();
			tradeService.delete(savedTrade);
		Optional<Trade> trade = tradeService.findById(tradeId);

		Assert.assertFalse(trade.isPresent());
	}
}