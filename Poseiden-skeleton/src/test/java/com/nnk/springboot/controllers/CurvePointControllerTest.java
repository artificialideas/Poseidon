package com.nnk.springboot.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.service.CurvePointService;
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
public class CurvePointControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CurvePointService curvePointService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@WithMockUser(username="admin",roles={"USER","ADMIN"})
	@DisplayName("GET - List all curvePoints REST API //home()")
	public void givenListOfCurvePoints_whenFindAllCurvePoint_thenReturnCurvePointsList() throws Exception {
		// given - precondition or setup
		List<CurvePoint> listOfCurves = new ArrayList<>();
			listOfCurves.add(new CurvePoint(10, 10d, 30d));
			listOfCurves.add(new CurvePoint(20, 20d, 60d));
		given(curvePointService.findAll()).willReturn(listOfCurves);

		// when -  action or the behaviour that we are going test
		ResultActions response = mockMvc.perform(get("/curvePoint/list"));

		// then - verify the output
		response.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.size()",
						is(listOfCurves.size())));
	}

	@Test
	@WithMockUser(username="admin",roles={"USER","ADMIN"})
	@DisplayName("POST - Create new curvePoint REST API //validate()")
	public void givenCurvePointObject_whenCreateCurvePoint_thenReturnSavedCurvePoint() throws Exception {
		// given - precondition or setup
		CurvePoint curve = new CurvePoint(10, 10d, 30d);
		curvePointService.save(curve);

		// when - action or behaviour that we are going test
		ResultActions response = mockMvc.perform(post("/curvePoint/validate")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(curve)));

		// then - verify the result or output using assert statements
		response.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.curveId",
						is(curve.getCurveId())))
				.andExpect(jsonPath("$.term",
						is(curve.getTerm())))
				.andExpect(jsonPath("$.value",
						is(curve.getValue())));
	}

	@Test
	@WithMockUser(username="admin",roles={"USER","ADMIN"})
	@DisplayName("POST - Update curvePoint REST API -> positive scenario //updateCurvePoint()")
	public void givenUpdatedCurvePoint_whenUpdateCurvePoint_thenReturnUpdateCurvePointObject() throws Exception {
		// given - precondition or setup
		int curveId = 1;
		CurvePoint savedCurve = new CurvePoint(10, 10d, 30d);
		CurvePoint updatedCurve = new CurvePoint(11, 11d, 31d);

		given(curvePointService.findById(curveId)).willReturn(Optional.of(savedCurve));
		curvePointService.save(updatedCurve);

		// when -  action or the behaviour that we are going test
		ResultActions response = mockMvc.perform(post("/curvePoint/update/{id}", curveId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedCurve)));

		// then - verify the output
		response.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.curveId",
						is(updatedCurve.getCurveId())))
				.andExpect(jsonPath("$.term",
						is(updatedCurve.getTerm())))
				.andExpect(jsonPath("$.value",
						is(updatedCurve.getValue())));
	}

	@Test
	@WithMockUser(username="admin",roles={"USER","ADMIN"})
	@DisplayName("POST - Update curvePoint REST API -> negative scenario //updateCurvePoint()")
	public void givenUpdatedCurvePoint_whenUpdateCurvePoint_thenReturn404() throws Exception {
		// given - precondition or setup
		int curveId = 1;
		CurvePoint savedCurve = new CurvePoint(10, 10d, 30d);
		CurvePoint updatedCurve = new CurvePoint(11, 11d, 31d);

		given(curvePointService.findById(curveId)).willReturn(Optional.empty());
		curvePointService.save(updatedCurve);

		// when -  action or the behaviour that we are going test
		ResultActions response = mockMvc.perform(post("/curvePoint/update/{id}", curveId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedCurve)));

		// then - verify the output
		response.andExpect(status().isNotFound())
				.andDo(print());
	}

	@Test
	@WithMockUser(username="admin",roles={"USER","ADMIN"})
	@DisplayName("GET - Delete curvePoint REST API //deleteCurvePoint()")
	public void givenCurvePointObject_whenDeleteCurvePoint_thenReturn200() throws Exception {
		// given - precondition or setup
		CurvePoint curve = new CurvePoint(10, 10d, 30d);
		willDoNothing().given(curvePointService).delete(curve);

		// when -  action or the behaviour that we are going test
		ResultActions response = mockMvc.perform(get("/curvePoint/delete/{id}", curve.getId()));

		// then - verify the output
		response.andExpect(status().isOk())
				.andDo(print());
	}
}
