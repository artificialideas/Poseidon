package com.nnk.springboot.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.service.CurvePointService;
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
public class CurvePointControllerTest {
	private final int ID = 1;
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

	@Test
	@WithMockUser(username="admin",roles={"USER","ADMIN"})
	@DisplayName("GET - List all curvePoints REST API //home()")
	public void givenListOfCurvePoints_whenFindAllCurvePoint_thenReturnCurvePointsList() throws Exception {
		mockMvc.perform(get("/curvePoint/list"))
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.size()", is(1)));
	}

	@Test
	@WithMockUser(username="admin",roles={"USER","ADMIN"})
	@DisplayName("POST - Create new curvePoint REST API //validate()")
	public void givenCurvePointObject_whenCreateCurvePoint_thenReturnSavedCurvePoint() throws Exception {
		mockMvc.perform(post("/curvePoint/validate")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(curve)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.curveId", is(curve.getCurveId())))
				.andExpect(jsonPath("$.term", is(curve.getTerm())))
				.andExpect(jsonPath("$.value", is(curve.getValue())));
	}

	@Test
	@WithMockUser(username="admin",roles={"USER","ADMIN"})
	@DisplayName("POST - Update curvePoint REST API -> positive scenario //updateCurvePoint()")
	public void givenUpdatedCurvePoint_whenUpdateCurvePoint_thenReturnUpdateCurvePointObject() throws Exception {
		curve.setCurveId(11);
		curve.setTerm(11d);
		curve.setValue(31d);

		mockMvc.perform(post("/curvePoint/update/" + ID)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(curve)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.curveId", is(curve.getCurveId())))
				.andExpect(jsonPath("$.term", is(curve.getTerm())))
				.andExpect(jsonPath("$.value", is(curve.getValue())));
	}

	@Test
	@WithMockUser(username="admin",roles={"USER","ADMIN"})
	@DisplayName("POST - Update curvePoint REST API -> negative scenario //updateCurvePoint()")
	public void givenUpdatedCurvePoint_whenUpdateCurvePoint_thenReturn404() throws Exception {
		CurvePoint updatedCurve = new CurvePoint(12, 12d, 32d);

		given(curvePointService.findById(ID)).willReturn(Optional.empty());
			curvePointService.save(updatedCurve);

		mockMvc.perform(post("/curvePoint/update/" + ID)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(updatedCurve)))
				.andExpect(status().isNotFound())
				.andDo(print());

		curvePointService.delete(updatedCurve);
	}

	@Test
	@WithMockUser(username="admin",roles={"USER","ADMIN"})
	@DisplayName("GET - Delete curvePoint REST API //deleteCurvePoint()")
	public void givenCurvePointObject_whenDeleteCurvePoint_thenReturn200() throws Exception {
		mockMvc.perform(get("/curvePoint/delete/" + curve.getId()))
				.andExpect(status().isOk())
				.andDo(print());
	}
}
