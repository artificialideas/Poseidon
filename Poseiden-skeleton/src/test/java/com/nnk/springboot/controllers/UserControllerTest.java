package com.nnk.springboot.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.service.UserService;
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
public class UserControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@WithMockUser(username="admin",roles={"ADMIN"})
	@DisplayName("GET - List all user REST API -> positive scenario //home()")
	public void givenListOfUsers_whenFindAllUserWithAuthorizedRole_thenReturnUsersList() throws Exception {
		// given - precondition or setup
		List<User> listOfUsers = new ArrayList<>();
		listOfUsers.add(new User("alfa", "$2a$12$roCCv37fd2rF3m4TQMhMNOlCLwCFEVUKC6W2RWJDfH0AhGDHEWEI2", "Alfa TESTER", "ADMIN"));
		listOfUsers.add(new User("beta", "$2a$12$BdE98BteOG43QDuCEp7FwOaOFGoHBOfJCY/iiKzstcvrh/mhJxHBy", "Beta TESTER", "USER"));
		given(userService.findAll()).willReturn(listOfUsers);

		// when -  action or the behaviour that we are going test
		ResultActions response = mockMvc.perform(get("/user/list"));

		// then - verify the output
		response.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.size()",
						is(listOfUsers.size())));
	}

	@Test
	@WithMockUser(username="user",roles={"USER"})
	@DisplayName("GET - List all user REST API -> negative scenario //home()")
	public void givenListOfUsers_whenFindAllUserWithoutAuthorizedRole_thenReturn403() throws Exception {
		// given - precondition or setup
		List<User> listOfUsers = new ArrayList<>();
			listOfUsers.add(new User("alfa", "$2a$12$roCCv37fd2rF3m4TQMhMNOlCLwCFEVUKC6W2RWJDfH0AhGDHEWEI2", "Alfa TESTER", "ADMIN"));
			listOfUsers.add(new User("beta", "$2a$12$BdE98BteOG43QDuCEp7FwOaOFGoHBOfJCY/iiKzstcvrh/mhJxHBy", "Beta TESTER", "USER"));
		given(userService.findAll()).willReturn(listOfUsers);

		// when -  action or the behaviour that we are going test
		ResultActions response = mockMvc.perform(get("/user/list"));

		// then - verify the output
		response.andExpect(status().isUnauthorized())
				.andDo(print());
	}

	@Test
	@WithMockUser(username="admin",roles={"ADMIN"})
	@DisplayName("POST - Create new user REST API //validate()")
	public void givenUserObject_whenCreateUser_thenReturnSavedUser() throws Exception {
		// given - precondition or setup
		User user = new User("alfa", "$2a$12$roCCv37fd2rF3m4TQMhMNOlCLwCFEVUKC6W2RWJDfH0AhGDHEWEI2", "Alfa TESTER", "ADMIN");
		userService.save(user);

		// when - action or behaviour that we are going test
		ResultActions response = mockMvc.perform(post("/user/validate")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(user)));

		// then - verify the result or output using assert statements
		response.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.fullname",
						is(user.getFullname())))
				.andExpect(jsonPath("$.username",
						is(user.getUsername())))
				.andExpect(jsonPath("$.password",
						is(user.getPassword())))
				.andExpect(jsonPath("$.role",
						is(user.getRole())));
	}

	@Test
	@WithMockUser(username="admin",roles={"ADMIN"})
	@DisplayName("POST - Update user REST API -> positive scenario //updateUser()")
	public void givenUpdatedUser_whenUpdateUser_thenReturnUpdateUserObject() throws Exception {
		// given - precondition or setup
		int userId = 1;
		User savedUser = new User("alfa", "$2a$12$roCCv37fd2rF3m4TQMhMNOlCLwCFEVUKC6W2RWJDfH0AhGDHEWEI2", "Alfa TESTER", "ADMIN");
		User updatedUser = new User("tester", "$2a$12$NnNNhZqbTN9/4MrThLfkpumaW.Ut1uhWfCOACXtiWt3/mJqDZYZ52", "Tester TESTER", "USER");

		given(userService.findById(userId)).willReturn(Optional.of(savedUser));
		userService.save(updatedUser);

		// when -  action or the behaviour that we are going test
		ResultActions response = mockMvc.perform(post("/user/update/{id}", userId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedUser)));

		// then - verify the output
		response.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.fullname",
						is(updatedUser.getFullname())))
				.andExpect(jsonPath("$.username",
						is(updatedUser.getUsername())))
				.andExpect(jsonPath("$.password",
						is(updatedUser.getPassword())))
				.andExpect(jsonPath("$.role",
						is(updatedUser.getRole())));
	}

	@Test
	@WithMockUser(username="admin",roles={"ADMIN"})
	@DisplayName("POST - Update user REST API -> negative scenario //updateUser()")
	public void givenUpdatedUser_whenUpdateUser_thenReturn404() throws Exception {
		// given - precondition or setup
		int userId = 1;
		User savedUser = new User("alfa", "$2a$12$roCCv37fd2rF3m4TQMhMNOlCLwCFEVUKC6W2RWJDfH0AhGDHEWEI2", "Alfa TESTER", "ADMIN");
		User updatedUser = new User("tester", "$2a$12$NnNNhZqbTN9/4MrThLfkpumaW.Ut1uhWfCOACXtiWt3/mJqDZYZ52", "Tester TESTER", "USER");

		given(userService.findById(userId)).willReturn(Optional.empty());
		userService.save(updatedUser);

		// when -  action or the behaviour that we are going test
		ResultActions response = mockMvc.perform(post("/User/update/{id}", userId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedUser)));

		// then - verify the output
		response.andExpect(status().isNotFound())
				.andDo(print());
	}

	@Test
	@WithMockUser(username="admin",roles={"ADMIN"})
	@DisplayName("GET - Delete user REST API //deleteUser()")
	public void givenUserObject_whenDeleteUser_thenReturn200() throws Exception {
		// given - precondition or setup
		User user = new User("alfa", "$2a$12$roCCv37fd2rF3m4TQMhMNOlCLwCFEVUKC6W2RWJDfH0AhGDHEWEI2", "Alfa TESTER", "ADMIN");
		willDoNothing().given(userService).delete(user);

		// when -  action or the behaviour that we are going test
		ResultActions response = mockMvc.perform(get("/User/delete/{id}", user.getId()));

		// then - verify the output
		response.andExpect(status().isOk())
				.andDo(print());
	}
}
