package com.nnk.springboot.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.service.UserService;
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
public class UserControllerTest {
	private User user;

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private UserService userService;

	@Before
	public void setup() {
		user = new User("tester", "$2a$12$NnNNhZqbTN9/4MrThLfkpumaW.Ut1uhWfCOACXtiWt3/mJqDZYZ52", "Tester TESTER", "USER");
			userService.save(user);
	}

	@After
	public void tearDown() {
		userService.delete(user);
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	@DisplayName("GET - List all user REST API -> positive scenario //home()")
	public void givenListOfUsers_whenFindAllUserWithAuthorizedRole_thenReturnUsersList() throws Exception {
		mockMvc.perform(get("/user/list")
						.contentType(MediaType.TEXT_HTML))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
	}

	@Test
	@WithMockUser(authorities = "USER")
	@DisplayName("GET - List all user REST API -> negative scenario //home()")
	public void givenListOfUsers_whenFindAllUserWithoutAuthorizedRole_thenReturn403() throws Exception {
		mockMvc.perform(get("/user/list")
					.contentType(MediaType.TEXT_HTML))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	@DisplayName("POST - Create new user REST API //validate()")
	public void givenUserObject_whenCreateUser_thenReturnSavedUser() throws Exception {
		mockMvc.perform(post("/user/validate")
						.param("fullname", "Beta TESTER")
						.param("username", "beta")
						.param("password", "betaTESTER12!")
						.param("role", "USER")
						.with(csrf()))
				.andExpect(redirectedUrl("/user/list"));
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	@DisplayName("POST - Update user REST API -> positive scenario //updateUser()")
	public void givenUpdatedUser_whenUpdateUser_thenReturnUpdateUserObject() throws Exception {
		mockMvc.perform(post("/user/update/" + user.getId())
						.param("fullname", "Alfa TESTER")
						.param("username", "alfa")
						.param("password", "$2a$12$roCCv37fd2rF3m4TQMhMNOlCLwCFEVUKC6W2RWJDfH0AhGDHEWEI2")
						.param("role", "ADMIN")
						.with(csrf()))
				.andExpect(redirectedUrl("/user/list"));
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	@DisplayName("POST - Update user REST API -> negative scenario //updateUser()")
	public void givenUpdatedUser_whenUpdateUser_thenReturn404() throws Exception {
		mockMvc.perform(post("/User/update/" + user.getId())
						.param("fullname", "Alfa TESTER")
						.param("username", "alfa")
						.param("password", "")
						.param("role", "ADMIN")
						.with(csrf()))
				.andExpect(model().hasErrors());
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	@DisplayName("GET - Delete user REST API //deleteUser()")
	public void givenUserObject_whenDeleteUser_thenReturn200() throws Exception {
		User newUser = new User("gamma", "$2a$12$OV.DUhyeZIb9ubdKXI5dLOS3Q.MU6xQwk0cnViHgDUJu1M5LeV2ra", "Gamma TESTER", "USER");
			userService.save(newUser);

		mockMvc.perform(get("/User/delete/" + newUser.getId()))
				.andExpect(redirectedUrl("/user/list"));
	}
}
