package com.nnk.springboot.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RatingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RatingRepository ratingRepository;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void shouldReturnRatingView() throws Exception {
        mockMvc.perform(get("/rating/list"))
                .andExpect(view().name("rating/list"))
                .andExpect(model().attributeExists("ratings"));
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void shouldReturnCreated() throws Exception {

        mockMvc.perform(post("/rating/validate")
                        .with(csrf())
                        .param("moodysRating", "10")
                        .param("sandPRating", "9")
                        .param("fitchRating", "1")
                        .param("orderNumber", "5"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void shouldReturnTheRatingById() throws Exception {

        mockMvc.perform(get("/rating/update/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/update"))
                .andExpect(model().attribute("rating", hasProperty("moodysRating", is("10"))))
                .andExpect(model().attribute("rating", hasProperty("sandPRating", is("9"))))
                .andExpect(model().attribute("rating", hasProperty("fitchRating", is("7"))))
                .andExpect(model().attribute("rating", hasProperty("orderNumber", is(4))));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void shouldReturnRedirectionForUpdating() throws Exception {

        mockMvc.perform(post("/rating/update/{id}", 2)
                        .with(csrf())
                        .param("moodysRating", "9")
                        .param("sandPRating", "10")
                        .param("fitchRating", "4")
                        .param("orderNumber", "3"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));

        Rating updatedRating = ratingRepository.findById(2).orElseThrow();
        assertEquals("9", updatedRating.getMoodysRating());
        assertEquals("10", updatedRating.getSandPRating());
        assertEquals("4", updatedRating.getFitchRating());
        assertEquals(3, updatedRating.getOrderNumber());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void shouldDeleteTheCurvePointSelected() throws Exception {

        mockMvc.perform(get("/rating/delete/{id}", 3))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));

        boolean deletedRating = ratingRepository.existsById(3);
        assertEquals(false, deletedRating);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void shouldSendErrorIdIsNotFound() throws Exception {

        mockMvc.perform(get("/rating/delete/{id}", 100))
                .andExpect(status().isNotFound());

        boolean deletedRating = ratingRepository.existsById(100);
        assertFalse(deletedRating);
    }

    @Test
    @WithMockUser(username = "user", roles = "User")
    public void shouldReturnUpdateAfterFailedUpdateRating() throws Exception {
        mockMvc.perform(post("/rating/update/{id}", 1)
                        .with(csrf())
                        .param("moodysRating", "")
                        .param("sandPRating", "")
                        .param("fitchRating", "")
                        .param("orderNumber", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/update"))
                .andExpect(model().attributeHasFieldErrors("rating", "moodysRating", "sandPRating", "fitchRating", "orderNumber"));
    }
}

