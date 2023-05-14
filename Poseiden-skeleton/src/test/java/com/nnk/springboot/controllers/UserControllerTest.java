package com.nnk.springboot.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.service.UserService;
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
public class UserControllerTest {
	private final int ID = 1;
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

	@Test
	@WithMockUser(username="admin",roles={"ADMIN"})
	@DisplayName("GET - List all user REST API -> positive scenario //home()")
	public void givenListOfUsers_whenFindAllUserWithAuthorizedRole_thenReturnUsersList() throws Exception {
		mockMvc.perform(get("/user/list"))
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.size()", is(3)));
	}

	@Test
	@WithMockUser(username="user",roles={"USER"})
	@DisplayName("GET - List all user REST API -> negative scenario //home()")
	public void givenListOfUsers_whenFindAllUserWithoutAuthorizedRole_thenReturn403() throws Exception {
		mockMvc.perform(get("/user/list"))
				.andExpect(status().isUnauthorized())
				.andDo(print());
	}

	@Test
	@WithMockUser(username="admin",roles={"ADMIN"})
	@DisplayName("POST - Create new user REST API //validate()")
	public void givenUserObject_whenCreateUser_thenReturnSavedUser() throws Exception {
		mockMvc.perform(post("/user/validate")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(user)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.fullname", is(user.getFullname())))
				.andExpect(jsonPath("$.username", is(user.getUsername())))
				.andExpect(jsonPath("$.password", is(user.getPassword())))
				.andExpect(jsonPath("$.role", is(user.getRole())));
	}

	@Test
	@WithMockUser(username="admin",roles={"ADMIN"})
	@DisplayName("POST - Update user REST API -> positive scenario //updateUser()")
	public void givenUpdatedUser_whenUpdateUser_thenReturnUpdateUserObject() throws Exception {
		user.setUsername("alfa");
		user.setPassword("$2a$12$roCCv37fd2rF3m4TQMhMNOlCLwCFEVUKC6W2RWJDfH0AhGDHEWEI2");
		user.setFullname("Alfa TESTER");
		user.setRole("ADMIN");

		mockMvc.perform(post("/user/update/" + ID)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(user)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.fullname", is(user.getFullname())))
				.andExpect(jsonPath("$.username", is(user.getUsername())))
				.andExpect(jsonPath("$.password", is(user.getPassword())))
				.andExpect(jsonPath("$.role", is(user.getRole())));
	}

	@Test
	@WithMockUser(username="admin",roles={"ADMIN"})
	@DisplayName("POST - Update user REST API -> negative scenario //updateUser()")
	public void givenUpdatedUser_whenUpdateUser_thenReturn404() throws Exception {
		User updatedUser = new User("beta", "$2a$12$OV.DUhyeZIb9ubdKXI5dLOS3Q.MU6xQwk0cnViHgDUJu1M5LeV2ra", "Beta TESTER", "USER");

		given(userService.findById(ID)).willReturn(Optional.empty());
			userService.save(updatedUser);

		mockMvc.perform(post("/User/update/" + ID)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(updatedUser)))
				.andExpect(status().isNotFound())
				.andDo(print());

		userService.delete(updatedUser);
	}

	@Test
	@WithMockUser(username="admin",roles={"ADMIN"})
	@DisplayName("GET - Delete user REST API //deleteUser()")
	public void givenUserObject_whenDeleteUser_thenReturn200() throws Exception {
		mockMvc.perform(get("/User/delete/{id}", user.getId()))
				.andExpect(status().isOk())
				.andDo(print());
	}
}
