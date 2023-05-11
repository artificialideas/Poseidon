package com.nnk.springboot.service;

import com.nnk.springboot.domain.User;
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
public class UserServiceTest {
	@Autowired
	private UserService userService;

	@Test
	@Order(1)
	@DisplayName("Save user //save()")
	public void givenNewUser_whenCreateUser_thenReturnUserObject() {
		User user = new User("tester", "$2a$12$z4pEKxpUguejxE5AVLBSpe2rLJ8efjtgfQEJsjttVm1zlRmIlxQEu", "Tester TESTER", "USER");
			userService.save(user);
		User savedUser = userService.findById(user.getId()).get();

		assertNotNull(savedUser.getId());
		assertEquals("tester", savedUser.getUsername());
	}

	@Test
	@Order(2)
	@DisplayName("Find user //findAll()")
	public void givenListOfUsers_whenFindAllUser_thenReturnUsersList() {
		List<User> listResult = userService.findAll();

		assertTrue(listResult.size() > 0);
	}

	@Test
	@Order(3)
	@DisplayName("Find user //findById()")
	public void givenUser_whenFindByIdUser_thenReturnUserObject() {
		int userId = 3;
		User user = userService.findById(userId).get();

		assertEquals(Optional.ofNullable(user.getId()), Optional.of(3));
	}

	@Test
	@Order(4)
	@DisplayName("Update user //save()")
	public void givenExistentUser_whenUpdateUser_thenReturnUserObject() {
		int UserId = 3;
		User savedUser = userService.findById(UserId).get();
			savedUser.setUsername("alfa");
			userService.save(savedUser);
		User updatedUser = userService.findById(savedUser.getId()).get();

		assertEquals("alfa", updatedUser.getUsername());
	}

	@Test
	@Order(5)
	@DisplayName("Delete user //delete()")
	public void givenUserObject_whenDeleteUser_thenReturn200() {
		int userId = 3;
		User savedUser = userService.findById(userId).get();
			userService.delete(savedUser);
		Optional<User> user = userService.findById(userId);

		assertFalse(user.isPresent());
	}
}