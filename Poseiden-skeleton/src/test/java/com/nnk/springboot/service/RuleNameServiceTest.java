package com.nnk.springboot.service;

import com.nnk.springboot.domain.RuleName;
import org.junit.After;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RuleNameServiceTest {
	@Autowired
	private RuleNameService ruleNameService;

	private RuleName ruleName;

	@Before
	public void setUp() {
		ruleName = new RuleName("Rule Name 1", "Description 1", "Json 1", "Template 1", "SQL 1", "SQL Part 1");
		ruleNameService.save(ruleName);
	}

	@After
	public void tearDown() {
		if (ruleNameService.findById(ruleName.getId()).isPresent()) {
			ruleNameService.delete(ruleName);
		}
	}

	@Test
	@Order(1)
	@DisplayName("Save ruleName //save()")
	public void givenNewRuleName_whenCreateRuleName_thenReturnRuleNameObject() {
		RuleName savedRule = ruleNameService.findById(ruleName.getId()).get();

		assertNotNull(savedRule.getId());
		assertEquals("Rule Name 1", savedRule.getName());
	}

	@Test
	@Order(2)
	@DisplayName("Find ruleName //findAll()")
	public void givenListOfRuleNames_whenFindAllRuleName_thenReturnRuleNamesList() {
		List<RuleName> listResult = ruleNameService.findAll();

		assertTrue(listResult.size() > 0);
	}

	@Test
	@Order(3)
	@DisplayName("Find ruleName //findById()")
	public void givenRuleName_whenFindByIdRuleName_thenReturnRuleNameObject() {
		int ruleId = ruleName.getId();
		RuleName ruleName = ruleNameService.findById(ruleId).get();

		assertEquals(Optional.ofNullable(ruleName.getId()), Optional.of(ruleId));
	}

	@Test
	@Order(4)
	@DisplayName("Update ruleName //save()")
	public void givenExistentRuleName_whenUpdateRuleName_thenReturnRuleNameObject() {
		int ruleId = ruleName.getId();
		RuleName savedRule = ruleNameService.findById(ruleId).get();
			savedRule.setName("Rule Name 0");
			ruleNameService.save(savedRule);
		RuleName updatedRule = ruleNameService.findById(savedRule.getId()).get();

		assertEquals("Rule Name 0", updatedRule.getName());
	}

	@Test
	@Order(5)
	@DisplayName("Delete ruleName //delete()")
	public void givenRuleNameObject_whenDeleteRuleName_thenReturn200() {
		int ruleId = ruleName.getId();
		RuleName savedRule = ruleNameService.findById(ruleId).get();
			ruleNameService.delete(savedRule);
		Optional<RuleName> ruleName = ruleNameService.findById(ruleId);

		assertFalse(ruleName.isPresent());
	}
}
