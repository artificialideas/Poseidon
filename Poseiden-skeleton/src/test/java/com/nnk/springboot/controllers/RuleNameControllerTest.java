package com.nnk.springboot.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.service.RuleNameService;
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
public class RuleNameControllerTest {
	private final int ID = 1;
	private RuleName rule;

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private RuleNameService ruleNameService;

	@Before
	public void setup() {
		rule = new RuleName("Rule Name ", "Description ", "Json ", "Template", "SQL", "SQL Part");
		ruleNameService.save(rule);
	}

	@Test
	@WithMockUser(username="admin",roles={"USER","ADMIN"})
	@DisplayName("GET - List all ruleName REST API //home()")
	public void givenListOfRuleNames_whenFindAllRuleName_thenReturnRuleNamesList() throws Exception {
		mockMvc.perform(get("/ruleName/list"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.size()", is(1)));
	}

	@Test
	@WithMockUser(username="admin",roles={"USER","ADMIN"})
	@DisplayName("POST - Create new ruleName REST API //validate()")
	public void givenRuleNameObject_whenCreateRuleName_thenReturnSavedRuleName() throws Exception {
		mockMvc.perform(post("/ruleName/validate")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(rule)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.name", is(rule.getName())))
				.andExpect(jsonPath("$.description", is(rule.getDescription())))
				.andExpect(jsonPath("$.json", is(rule.getJson())))
				.andExpect(jsonPath("$.template", is(rule.getTemplate())))
				.andExpect(jsonPath("$.sqlStr", is(rule.getSqlStr())))
				.andExpect(jsonPath("$.sqlPart", is(rule.getSqlPart())));
	}

	@Test
	@WithMockUser(username="admin",roles={"USER","ADMIN"})
	@DisplayName("POST - Update ruleName REST API -> positive scenario //updateRule()")
	public void givenUpdatedRuleName_whenUpdateRuleName_thenReturnUpdateRuleNameObject() throws Exception {
		rule.setName("Rule");
		rule.setDescription("Des");
		rule.setJson("JSON");
		rule.setTemplate("Tmpl");
		rule.setSqlStr("SQLanguage");
		rule.setSqlPart("SQL sequel");

		mockMvc.perform(post("/ruleName/update/" + ID)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(rule)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is(rule.getName())))
				.andExpect(jsonPath("$.description", is(rule.getDescription())))
				.andExpect(jsonPath("$.json", is(rule.getJson())))
				.andExpect(jsonPath("$.template", is(rule.getTemplate())))
				.andExpect(jsonPath("$.sqlStr", is(rule.getSqlStr())))
				.andExpect(jsonPath("$.sqlPart", is(rule.getSqlPart())));
	}

	@Test
	@WithMockUser(username="admin",roles={"USER","ADMIN"})
	@DisplayName("POST - Update ruleName REST API -> negative scenario //updateRule()")
	public void givenUpdatedRuleName_whenUpdateRuleName_thenReturn404() throws Exception {
		RuleName updatedRule = new RuleName("Rule 1", "Des 1", "JSON 1", "Tmpl 1", "SQLanguage 1", "SQL sequel 1");

		given(ruleNameService.findById(ID)).willReturn(Optional.empty());
			ruleNameService.save(updatedRule);

		mockMvc.perform(post("/ruleName/update/" + ID)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(updatedRule)))
				.andExpect(status().isNotFound())
				.andDo(print());

		ruleNameService.delete(updatedRule);
	}

	@Test
	@WithMockUser(username="admin",roles={"USER","ADMIN"})
	@DisplayName("GET - Delete ruleName REST API //deleteRule()")
	public void givenRuleNameObject_whenDeleteRuleName_thenReturn200() throws Exception {
		mockMvc.perform(get("/ruleName/delete/" + rule.getId()))
				.andExpect(status().isOk())
				.andDo(print());
	}
}
