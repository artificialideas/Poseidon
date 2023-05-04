package com.nnk.springboot.service;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
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
public class UserServiceTest {

	@Autowired
	private static UserRepository userRepository;

	@BeforeAll
	public static void setUp() {
		User user1 = new User("alfa", "$2a$12$roCCv37fd2rF3m4TQMhMNOlCLwCFEVUKC6W2RWJDfH0AhGDHEWEI2", "Alfa TESTER", "ADMIN");
			userRepository.save(user1);
		User user2 = new User("beta", "$2a$12$BdE98BteOG43QDuCEp7FwOaOFGoHBOfJCY/iiKzstcvrh/mhJxHBy", "Beta TESTER", "USER");
			userRepository.save(user2);
	}

	@Test
	@DisplayName("Save user //save()")
	public void givenNewUser_whenCreateUser_thenReturnUserObject() {
		User user = new User("tester", "$2a$12$NnNNhZqbTN9/4MrThLfkpumaW.Ut1uhWfCOACXtiWt3/mJqDZYZ52", "Tester TESTER", "USER");
			userRepository.save(user);
		User savedUser = userRepository.findById(user.getId()).get();

		Assert.assertNotNull(savedUser.getId());
		Assert.assertEquals("tester", savedUser.getUsername());
	}

	@Test
	@DisplayName("Update user //save()")
	public void givenExistentUser_whenUpdateUser_thenReturnUserObject() {
		int UserId = 0;
		User savedUser = userRepository.findById(UserId).get();
			savedUser.setUsername("gamma");
			userRepository.save(savedUser);
		User updatedUser = userRepository.findById(savedUser.getId()).get();

		Assert.assertEquals("gamma", updatedUser.getUsername());
	}

	@Test
	@DisplayName("Find user //findAll()")
	public void givenListOfUsers_whenFindAllUser_thenReturnUsersList() {
		List<User> listResult = userRepository.findAll();

		Assert.assertTrue(listResult.size() > 0);
	}

	@Test
	@DisplayName("Find user //findById()")
	public void givenUser_whenFindByIdUser_thenReturnUserObject() {
		int userId = 0;
		User user = userRepository.findById(userId).get();

		Assert.assertEquals(Optional.ofNullable(user.getId()), Optional.of(0));
	}

	@Test
	@DisplayName("Delete user //delete()")
	public void givenUserObject_whenDeleteUser_thenReturn200() {
		int userId = 0;
		User savedUser = userRepository.findById(userId).get();
			userRepository.delete(savedUser);
		Optional<User> user = userRepository.findById(userId);

		Assert.assertFalse(user.isPresent());
	}
}