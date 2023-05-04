package com.nnk.springboot.service;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TradeServiceTest {

	@Autowired
	private static TradeRepository tradeRepository;

	@BeforeAll
	public static void setUp() {
		Trade trade1 = new Trade("Trade Account 1", "Type 1", 10d);
			tradeRepository.save(trade1);
		Trade trade2 = new Trade("Trade Account 2", "Type 2", 20d);
			tradeRepository.save(trade2);
	}

	@Test
	@DisplayName("Save trade //save()")
	public void givenNewTrade_whenCreateTrade_thenReturnTradeObject() {
		Trade trade = new Trade("Trade Account 3", "Type 3", 30d);
			tradeRepository.save(trade);
		Trade savedTrade = tradeRepository.findById(trade.getId()).get();

		Assert.assertNotNull(savedTrade.getId());
		Assert.assertEquals(30d, savedTrade.getBuyQuantity(), 0.0);
	}

	@Test
	@DisplayName("Update trade //save()")
	public void givenExistentTrade_whenUpdateTrade_thenReturnTradeObject() {
		int tradeId = 0;
		Trade savedTrade = tradeRepository.findById(tradeId).get();
			savedTrade.setBuyQuantity(11d);
			tradeRepository.save(savedTrade);
		Trade updatedTrade = tradeRepository.findById(savedTrade.getId()).get();

		Assert.assertEquals(11d, updatedTrade.getBuyQuantity(), 0.0);
	}

	@Test
	@DisplayName("Find trade //findAll()")
	public void givenListOfTrades_whenFindAllTrade_thenReturnTradesList() {
		List<Trade> listResult = tradeRepository.findAll();

		Assert.assertTrue(listResult.size() > 0);
	}

	@Test
	@DisplayName("Find trade //findById()")
	public void givenTrade_whenFindByIdTrade_thenReturnTradeObject() {
		int tradeId = 0;
		Trade trade = tradeRepository.findById(tradeId).get();

		Assert.assertEquals(Optional.ofNullable(trade.getId()), Optional.of(0));
	}

	@Test
	@DisplayName("Delete trade //delete()")
	public void givenTradeObject_whenDeleteTrade_thenReturn200() {
		int tradeId = 0;
		Trade savedTrade = tradeRepository.findById(tradeId).get();
			tradeRepository.delete(savedTrade);
		Optional<Trade> trade = tradeRepository.findById(tradeId);

		Assert.assertFalse(trade.isPresent());
	}
}