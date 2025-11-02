package com.nnk.springboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.services.impl.JWTService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class LoginControllerTest {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserRepository userRepositorye;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void shouldReturnLoginView() throws Exception {
        mockMvc.perform(get("/app/login"))
                .andExpect(view().name("login"));
    }

    @Test
    void shouldReturnHomePage() throws Exception {
        mockMvc.perform(post("/app/signin")
                        .param("username", "leo")
                        .param("password", "Tewwe!1ddd()"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnUserListPage() throws Exception {
        mockMvc.perform(get("/app/secure/article-details"))
                .andExpect(model().attributeExists("users"))
                .andExpect(view().name("user/list"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void shouldReturnErrorPage() throws Exception {
        mockMvc.perform(get("/user/list"))
                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser(username = "badUser")
    void shouldReturnErrorMessagePage() throws Exception {
        mockMvc.perform(get("/app/error"))
                .andExpect(model().attribute("errorMsg", "You are not authorized for the requested data."))
                .andExpect(view().name("403"));
    }

}
