package com.nnk.springboot.service;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
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
public class CurvePointServiceTest {
	@Autowired
	private static CurvePointRepository curvePointRepository;

	@BeforeAll
	public static void setUp() {
		CurvePoint curve1 = new CurvePoint(10, 10d, 30d);
			curvePointRepository.save(curve1);
		CurvePoint curve2 = new CurvePoint(20, 20d, 60d);
			curvePointRepository.save(curve2);
	}

	@Test
	@DisplayName("Save curvePoint //save()")
	public void givenNewCurvePoint_whenCreateCurvePoint_thenReturnCurvePointObject(){
		CurvePoint curvePoint = new CurvePoint(30, 30d, 90d);
			curvePointRepository.save(curvePoint);
		CurvePoint savedCurve = curvePointRepository.findById(curvePoint.getId()).get();

		Assert.assertNotNull(savedCurve.getId());
		Assert.assertEquals(30, savedCurve.getCurveId(), 0.0);
	}

	@Test
	@DisplayName("Update curvePoint //save()")
	public void givenUpdatedCurvePoint_whenUpdateCurvePoint_thenReturnCurvePointObject() {
		int curveId = 0;
		CurvePoint savedCurve = curvePointRepository.findById(curveId).get();
			savedCurve.setCurveId(11);
			curvePointRepository.save(savedCurve);
		CurvePoint updatedCurve = curvePointRepository.findById(savedCurve.getId()).get();

		Assert.assertEquals(11, updatedCurve.getCurveId(), 0.0);
	}

	@Test
	@DisplayName("Find curvePoint //findAll()")
	public void givenListOfCurvePoints_whenFindAllCurvePoint_thenReturnCurvePointsList() {
		List<CurvePoint> listResult = curvePointRepository.findAll();

		Assert.assertTrue(listResult.size() > 0);
	}

	@Test
	@DisplayName("Find curvePoint //findById()")
	public void givenCurvePoint_whenFindByIdCurvePoint_thenReturnCurvePointObject() {
		int curveId = 0;
		CurvePoint curvePoint = curvePointRepository.findById(curveId).get();

		Assert.assertEquals(Optional.ofNullable(curvePoint.getId()), Optional.of(0));
	}

	@Test
	@DisplayName("Delete curvePoint //delete()")
	public void givenCurvePointObject_whenDeleteCurvePoint_thenReturn200() {
		int curveId = 0;
		CurvePoint savedCurve = curvePointRepository.findById(curveId).get();
			curvePointRepository.delete(savedCurve);
		Optional<CurvePoint> curveList = curvePointRepository.findById(curveId);

		Assert.assertFalse(curveList.isPresent());
	}
}
