package com.nnk.springboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void ShouldReturnUserList() throws Exception {

        mockMvc.perform(get("/user/list"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("users"))
                .andExpect(view().name("user/list"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void shouldReturnUserAddPage() throws Exception {
        mockMvc.perform(get("/user/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void shouldReturnPageAddAndANewUSer() throws Exception {
        mockMvc.perform(post("/user/validate")
                        .with(csrf())
                        .param("username", "John")
                        .param("password", "RT@#/.ff3wd")
                        .param("fullname", "John lenon")
                        .param("role", "ADMIN"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

        Optional<User> newUser = userRepository.findByUsername("john");
        assertEquals("John", newUser.get().getUsername());
        assertEquals("John lenon", newUser.get().getFullname());
        assertEquals("ADMIN", newUser.get().getRole());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void shouldReturnUserAdd() throws Exception {
        mockMvc.perform(post("/user/validate")
                        .with(csrf())
                        .param("username", "John")
                        .param("password", "3wd")
                        .param("fullname", "John lenon")
                        .param("role", "ADMIN"))
                .andExpect(view().name("user/add"));
        Optional<User> newUser = userRepository.findByUsername("John");
        assertFalse(newUser.isPresent());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void shouldReturnASpecificUser() throws Exception {
        mockMvc.perform(get("/user/update/{id}", 1))
                .andExpect(view().name("user/update"))
                .andExpect(model().attributeExists("user"));

        User user = userRepository.findById(1).orElseThrow();
        assertEquals("Administrator", user.getFullname());
        assertEquals("ADMIN", user.getRole());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void shouldReturnAnUpdateUser() throws Exception {
        mockMvc.perform(post("/user/update/{id}", 1)
                        .with(csrf())
                        .param("username", "newUsername")
                        .param("password", "newPassword1!")
                        .param("fullname", "newFullname")
                        .param("role", "ADMIN"))
                .andExpect(status().is3xxRedirection());

        User user = userRepository.findByUsername("newUsername").orElseThrow();
        assertEquals("newUsername", user.getUsername());
        assertEquals("newFullname", user.getFullname());
    }
}
