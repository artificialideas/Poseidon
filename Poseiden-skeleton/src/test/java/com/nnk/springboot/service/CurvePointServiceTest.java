package com.nnk.springboot.service;

import com.nnk.springboot.domain.CurvePoint;
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
public class CurvePointServiceTest {
	@Autowired
	private CurvePointService curvePointService;

	private CurvePoint curvePoint;

	@Before
	public void setUp() {
		curvePoint = new CurvePoint(10, 10d, 30d);
		curvePointService.save(curvePoint);
	}

	@After
	public void tearDown() {
		if (curvePointService.findById(curvePoint.getId()).isPresent()) {
			curvePointService.delete(curvePoint);
		}
	}

	@Test
	@Order(1)
	@DisplayName("Save curvePoint //save()")
	public void givenNewCurvePoint_whenCreateCurvePoint_thenReturnCurvePointObject(){
		CurvePoint savedCurve = curvePointService.findById(curvePoint.getId()).get();

		assertNotNull(savedCurve.getId());
		assertEquals(10, savedCurve.getCurveId(), 0.0);
	}

	@Test
	@Order(2)
	@DisplayName("Find curvePoint //findAll()")
	public void givenListOfCurvePoints_whenFindAllCurvePoint_thenReturnCurvePointsList() {
		List<CurvePoint> listResult = curvePointService.findAll();

		assertTrue(listResult.size() > 0);
	}

	@Test
	@Order(3)
	@DisplayName("Find curvePoint //findById()")
	public void givenCurvePoint_whenFindByIdCurvePoint_thenReturnCurvePointObject() {
		int curveId = curvePoint.getId();
		CurvePoint curvePoint = curvePointService.findById(curveId).get();

		assertEquals(Optional.ofNullable(curvePoint.getId()), Optional.of(curveId));
	}

	@Test
	@Order(4)
	@DisplayName("Update curvePoint //save()")
	public void givenUpdatedCurvePoint_whenUpdateCurvePoint_thenReturnCurvePointObject() {
		int curveId = curvePoint.getId();
		CurvePoint savedCurve = curvePointService.findById(curveId).get();
			savedCurve.setCurveId(11);
			curvePointService.save(savedCurve);
		CurvePoint updatedCurve = curvePointService.findById(savedCurve.getId()).get();

		assertEquals(11, updatedCurve.getCurveId(), 0.0);
	}

	@Test
	@Order(5)
	@DisplayName("Delete curvePoint //delete()")
	public void givenCurvePointObject_whenDeleteCurvePoint_thenReturn200() {
		int curveId = curvePoint.getId();
		CurvePoint savedCurve = curvePointService.findById(curveId).get();
			curvePointService.delete(savedCurve);
		Optional<CurvePoint> curveList = curvePointService.findById(curveId);

		assertFalse(curveList.isPresent());
	}
}