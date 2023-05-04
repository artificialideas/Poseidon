package com.nnk.springboot.service;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
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
public class RuleNameServiceTest {

	@Autowired
	private static RuleNameRepository ruleNameRepository;

	@BeforeAll
	public static void setUp() {
		RuleName rule1 = new RuleName("Rule Name 1", "Description 1", "Json 1", "Template 1", "SQL 1", "SQL Part 1");
			ruleNameRepository.save(rule1);
		RuleName rule2 = new RuleName("Rule Name 2", "Description 2", "Json 2", "Template 2", "SQL 2", "SQL Part 2");
			ruleNameRepository.save(rule2);
	}

	@Test
	@DisplayName("Save ruleName //save()")
	public void givenNewRuleName_whenCreateRuleName_thenReturnRuleNameObject() {
		RuleName ruleName = new RuleName("Rule Name 3", "Description 3", "Json 3", "Template 3", "SQL 3", "SQL Part 3");
			ruleNameRepository.save(ruleName);
		RuleName savedRule = ruleNameRepository.findById(ruleName.getId()).get();

		Assert.assertNotNull(savedRule.getId());
		Assert.assertEquals("Rule Name 3", savedRule.getName());
	}

	@Test
	@DisplayName("Update ruleName //save()")
	public void givenExistentRuleName_whenUpdateRuleName_thenReturnRuleNameObject() {
		int ruleId = 0;
		RuleName savedRule = ruleNameRepository.findById(ruleId).get();
			savedRule.setName("Rule Name 0");
			ruleNameRepository.save(savedRule);
		RuleName updatedRule = ruleNameRepository.findById(savedRule.getId()).get();

		Assert.assertEquals("Rule Name 0", updatedRule.getName());
	}

	@Test
	@DisplayName("Find ruleName //findAll()")
	public void givenListOfRuleNames_whenFindAllRuleName_thenReturnRuleNamesList() {
		List<RuleName> listResult = ruleNameRepository.findAll();

		Assert.assertTrue(listResult.size() > 0);
	}

	@Test
	@DisplayName("Find ruleName //findById()")
	public void givenRuleName_whenFindByIdRuleName_thenReturnRuleNameObject() {
		int ruleId = 0;
		RuleName ruleName = ruleNameRepository.findById(ruleId).get();

		Assert.assertEquals(Optional.ofNullable(ruleName.getId()), Optional.of(0));
	}

	@Test
	@DisplayName("Delete ruleName //delete()")
	public void givenRuleNameObject_whenDeleteRuleName_thenReturn200() {
		int ruleId = 0;
		RuleName savedRule = ruleNameRepository.findById(ruleId).get();
			ruleNameRepository.delete(savedRule);
		Optional<RuleName> ruleName = ruleNameRepository.findById(ruleId);

		Assert.assertFalse(ruleName.isPresent());
	}
}
