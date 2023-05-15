package com.nnk.springboot.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.service.CurvePointService;
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
public class CurvePointControllerTest {
	private CurvePoint curve;

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private CurvePointService curvePointService;

	@Before
	public void setup() {
		curve = new CurvePoint(10, 10d, 30d);
			curvePointService.save(curve);
	}

	@After
	public void tearDown() {
		curvePointService.delete(curve);
	}

	@Test
	@WithMockUser
	@DisplayName("GET - List all curvePoints REST API //home()")
	public void givenListOfCurvePoints_whenFindAllCurvePoint_thenReturnCurvePointsList() throws Exception {
		mockMvc.perform(get("/curvePoint/list")
						.contentType(MediaType.TEXT_HTML))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
	}

	@Test
	@WithMockUser
	@DisplayName("POST - Create new curvePoint REST API //validate()")
	public void givenCurvePointObject_whenCreateCurvePoint_thenReturnSavedCurvePoint() throws Exception {
		mockMvc.perform(post("/curvePoint/validate")
						.param("curveId", "20")
						.param("term", "20d")
						.param("value", "60d")
						.with(csrf()))
				.andExpect(redirectedUrl("/curvePoint/list"));
	}

	@Test
	@WithMockUser
	@DisplayName("POST - Update curvePoint REST API -> positive scenario //updateCurvePoint()")
	public void givenUpdatedCurvePoint_whenUpdateCurvePoint_thenReturnUpdateCurvePointObject() throws Exception {
		mockMvc.perform(post("/curvePoint/update/" + curve.getId())
						.param("curveId", "11")
						.param("term", "11d")
						.param("value", "31d")
						.with(csrf()))
				.andExpect(redirectedUrl("/curvePoint/list"));
	}

	@Test
	@WithMockUser
	@DisplayName("POST - Update curvePoint REST API -> negative scenario //updateCurvePoint()")
	public void givenUpdatedCurvePoint_whenUpdateCurvePoint_thenReturn404() throws Exception {
		mockMvc.perform(post("/curvePoint/update/" + curve.getId())
						.param("curveId", "11")
						.param("term", "11d")
						.param("value", "31L")
						.with(csrf()))
				.andExpect(model().hasErrors());
	}

	@Test
	@WithMockUser
	@DisplayName("GET - Delete curvePoint REST API //deleteCurvePoint()")
	public void givenCurvePointObject_whenDeleteCurvePoint_thenReturn200() throws Exception {
		CurvePoint newCurve = new CurvePoint(12, 12d, 32d);
			curvePointService.save(newCurve);

		mockMvc.perform(get("/curvePoint/delete/" + newCurve.getId()))
				.andExpect(redirectedUrl("/curvePoint/list"));
	}
}
