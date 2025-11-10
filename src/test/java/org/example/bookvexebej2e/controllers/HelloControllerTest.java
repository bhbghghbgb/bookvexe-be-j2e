package org.example.bookvexebej2e.controllers;

import org.example.bookvexebej2e.models.dto.user.UserResponse;
import org.example.bookvexebej2e.services.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@DisplayName("Hello Controller Integration Tests")
class HelloControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private UserService userService;

    private UserResponse testUserResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        testUserResponse = new UserResponse();
        testUserResponse.setId(UUID.randomUUID());
        testUserResponse.setUsername("testuser");
        testUserResponse.setIsAdmin(false);
    }

    @Test
    @DisplayName("Should return hello message")
    void shouldReturnHelloMessage() throws Exception {
        // When & Then  
        mockMvc.perform(get("/hello"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Hello World"));
    }

    @Test
    @DisplayName("Should return user roles")
    @WithMockUser(roles = {"USER", "ADMIN"})
    void shouldReturnUserRoles() throws Exception {
        // When & Then
        mockMvc.perform(get("/roles"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("Should return current user information")
    @WithMockUser(username = "testuser")
    void shouldReturnCurrentUserInformation() throws Exception {
        // Given
        when(userService.getCurrentUser()).thenReturn(testUserResponse);

        // When & Then
        mockMvc.perform(get("/me"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.isAdmin").value(false));
    }

    @Test
    @DisplayName("Should handle service exception gracefully")
    @WithMockUser(username = "testuser")
    void shouldHandleServiceExceptionGracefully() throws Exception {
        // Given
        when(userService.getCurrentUser()).thenThrow(new RuntimeException("User not found"));

        // When & Then
        mockMvc.perform(get("/me"))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }
}