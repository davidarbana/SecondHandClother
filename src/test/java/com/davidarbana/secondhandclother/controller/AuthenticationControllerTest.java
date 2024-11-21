package com.davidarbana.secondhandclother.controller;

import com.davidarbana.secondhandclother.model.User;
import com.davidarbana.secondhandclother.repository.UserRepository;
import com.davidarbana.secondhandclother.security.JwtService;
import com.davidarbana.secondhandclother.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class AuthenticationControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthenticationController authenticationController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
    }

    @Test
    public void testRegisterUserSuccess() throws Exception {
        User user = new User(123L, "fullNameTest","addressTest","usernameTest","passwordTest");

        // Mock repository behavior to simulate that the user doesn't already exist
        when(userRepository.findByUsername("usernameTest")).thenReturn(Optional.empty());

        // Mock the behavior of the JWT service
        when(jwtService.generateToken("testuser")).thenReturn("mock-jwt-token");

        // Mock the userService register method
        Mockito.doNothing().when(userService).register(user);

        // Perform the register POST request
        mockMvc.perform(post("/auth/register")
                        .contentType("application/json")
                        .content("{ \"username\": \"usernameTest\", \"password\": \"passwordTest\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("mock-jwt-token")));  // Check if JWT token is returned
    }

    @Test
    public void testRegisterUserAlreadyExists() throws Exception {
        User user = new User(123L, "fullNameTest","addressTest","usernameTest","passwordTest");

        // Mock repository behavior to simulate that the user already exists
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        // Perform the register POST request and expect 400 Bad Request due to the user existing
        mockMvc.perform(post("/auth/register")
                        .contentType("application/json")
                        .content("{ \"username\": \"testuser\", \"password\": \"password123\"}"))
                .andExpect(status().isBadRequest()) // Expecting 400 due to exception thrown
                .andExpect(jsonPath("$", is("User already exists")));  // Check the error message
    }

    @Test
    public void testLoginUserSuccess() throws Exception {
        User user = new User(123L, "fullNameTest","addressTest","usernameTest","passwordTest");

        // Mock the login logic to simulate successful login and token generation
        when(userService.login(user)).thenReturn("mock-jwt-token");

        // Perform the login POST request
        mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content("{ \"username\": \"testuser\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("mock-jwt-token")));  // Check if JWT token is returned
    }

}
