package com.nnk.springboot.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.service.RuleNameService;
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
public class RuleNameControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private RuleNameService ruleNameService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@WithMockUser(username="admin",roles={"USER","ADMIN"})
	@DisplayName("GET - List all ruleName REST API //home()")
	public void givenListOfRuleNames_whenFindAllRuleName_thenReturnRuleNamesList() throws Exception {
		// given - precondition or setup
		List<RuleName> listOfRules = new ArrayList<>();
			listOfRules.add(new RuleName("Rule Name 1", "Description 1", "Json 1", "Template 1", "SQL 1", "SQL Part 1"));
			listOfRules.add(new RuleName("Rule Name 2", "Description 2", "Json 2", "Template 2", "SQL 2", "SQL Part 2"));
		given(ruleNameService.findAll()).willReturn(listOfRules);

		// when -  action or the behaviour that we are going test
		ResultActions response = mockMvc.perform(get("/ruleName/list"));

		// then - verify the output
		response.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.size()",
						is(listOfRules.size())));
	}

	@Test
	@WithMockUser(username="admin",roles={"USER","ADMIN"})
	@DisplayName("POST - Create new ruleName REST API //validate()")
	public void givenRuleNameObject_whenCreateRuleName_thenReturnSavedRuleName() throws Exception {
		// given - precondition or setup
		RuleName rule = new RuleName("Rule Name", "Description", "Json", "Template", "SQL", "SQL Part");
		ruleNameService.save(rule);

		// when - action or behaviour that we are going test
		ResultActions response = mockMvc.perform(post("/ruleName/validate")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(rule)));

		// then - verify the result or output using assert statements
		response.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.name",
						is(rule.getName())))
				.andExpect(jsonPath("$.description",
						is(rule.getDescription())))
				.andExpect(jsonPath("$.json",
						is(rule.getJson())))
				.andExpect(jsonPath("$.template",
						is(rule.getTemplate())))
				.andExpect(jsonPath("$.sqlStr",
						is(rule.getSqlStr())))
				.andExpect(jsonPath("$.sqlPart",
						is(rule.getSqlPart())));
	}

	@Test
	@WithMockUser(username="admin",roles={"USER","ADMIN"})
	@DisplayName("POST - Update ruleName REST API -> positive scenario //updateRule()")
	public void givenUpdatedRuleName_whenUpdateRuleName_thenReturnUpdateRuleNameObject() throws Exception {
		// given - precondition or setup
		int ruleId = 1;
		RuleName savedRule = new RuleName("Rule Name", "Description", "Json", "Template", "SQL", "SQL Part");
		RuleName updatedRule = new RuleName("Rule", "Des", "JSON", "Tmpl", "SQLanguage", "SQL sequel");

		given(ruleNameService.findById(ruleId)).willReturn(Optional.of(savedRule));
		ruleNameService.save(updatedRule);

		// when -  action or the behaviour that we are going test
		ResultActions response = mockMvc.perform(post("/ruleName/update/{id}", ruleId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedRule)));

		// then - verify the output
		response.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.name",
						is(updatedRule.getName())))
				.andExpect(jsonPath("$.description",
						is(updatedRule.getDescription())))
				.andExpect(jsonPath("$.json",
						is(updatedRule.getJson())))
				.andExpect(jsonPath("$.template",
						is(updatedRule.getTemplate())))
				.andExpect(jsonPath("$.sqlStr",
						is(updatedRule.getSqlStr())))
				.andExpect(jsonPath("$.sqlPart",
						is(updatedRule.getSqlPart())));
	}

	@Test
	@WithMockUser(username="admin",roles={"USER","ADMIN"})
	@DisplayName("POST - Update ruleName REST API -> negative scenario //updateRule()")
	public void givenUpdatedRuleName_whenUpdateRuleName_thenReturn404() throws Exception {
		// given - precondition or setup
		int ruleId = 1;
		RuleName savedRule = new RuleName("Rule Name", "Description", "Json", "Template", "SQL", "SQL Part");
		RuleName updatedRule = new RuleName("Rule", "Des", "JSON", "Tmpl", "SQLanguage", "SQL sequel");

		given(ruleNameService.findById(ruleId)).willReturn(Optional.empty());
		ruleNameService.save(updatedRule);

		// when -  action or the behaviour that we are going test
		ResultActions response = mockMvc.perform(post("/ruleName/update/{id}", ruleId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedRule)));

		// then - verify the output
		response.andExpect(status().isNotFound())
				.andDo(print());
	}

	@Test
	@WithMockUser(username="admin",roles={"USER","ADMIN"})
	@DisplayName("GET - Delete ruleName REST API //deleteRule()")
	public void givenRuleNameObject_whenDeleteRuleName_thenReturn200() throws Exception {
		// given - precondition or setup
		RuleName rule = new RuleName("Rule Name", "Description", "Json", "Template", "SQL", "SQL Part");
		willDoNothing().given(ruleNameService).delete(rule);

		// when -  action or the behaviour that we are going test
		ResultActions response = mockMvc.perform(get("/ruleName/delete/{id}", rule.getId()));

		// then - verify the output
		response.andExpect(status().isOk())
				.andDo(print());
	}
}
