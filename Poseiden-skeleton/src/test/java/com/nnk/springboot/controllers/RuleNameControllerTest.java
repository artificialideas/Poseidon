package com.nnk.springboot.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.service.RuleNameService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RuleNameControllerTest {
	private RuleName rule;

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private RuleNameService ruleNameService;

	@Before
	public void setup() {
		rule = new RuleName("Rule Name", "Description", "Json", "Template", "SQL", "SQL Part");
			ruleNameService.save(rule);
	}

	@After
	public void tearDown() {
		ruleNameService.delete(rule);
	}

	@Test
	@WithMockUser
	@DisplayName("GET - List all ruleName REST API //home()")
	public void givenListOfRuleNames_whenFindAllRuleName_thenReturnRuleNamesList() throws Exception {
		mockMvc.perform(get("/ruleName/list")
						.contentType(MediaType.TEXT_HTML))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
	}

	@Test
	@WithMockUser
	@DisplayName("POST - Create new ruleName REST API //validate()")
	public void givenRuleNameObject_whenCreateRuleName_thenReturnSavedRuleName() throws Exception {
		mockMvc.perform(post("/ruleName/validate")
						.param("name", "Rule Name")
						.param("description", "Description")
						.param("json", "Json")
						.param("template", "Template")
						.param("sqlStr", "SQL")
						.param("sqlPart", "SQL Part")
						.with(csrf()))
				.andExpect(redirectedUrl("/ruleName/list"));
	}

	@Test
	@WithMockUser
	@DisplayName("POST - Update ruleName REST API -> positive scenario //updateRule()")
	public void givenUpdatedRuleName_whenUpdateRuleName_thenReturnUpdateRuleNameObject() throws Exception {
		mockMvc.perform(post("/ruleName/update/" + rule.getId())
						.param("name", "Rule")
						.param("description", "Des")
						.param("json", "JSON")
						.param("template", "Tmpl")
						.param("sqlStr", "sql")
						.param("sqlPart", "sql 2")
						.with(csrf()))
				.andExpect(redirectedUrl("/ruleName/list"));
	}

	@Test
	@WithMockUser
	@DisplayName("GET - Delete ruleName REST API //deleteRule()")
	public void givenRuleNameObject_whenDeleteRuleName_thenReturn200() throws Exception {
		RuleName newRule = new RuleName("Rule 1", "Des 1", "JSON 1", "Tmpl 1", "SQLanguage 1", "SQL sequel 1");
			ruleNameService.save(newRule);

		mockMvc.perform(get("/ruleName/delete/" + newRule.getId()))
				.andExpect(redirectedUrl("/ruleName/list"));
	}
}
