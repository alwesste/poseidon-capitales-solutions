package com.nnk.springboot.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CurvepointControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CurvePointRepository curvePointRepository;

    @Test
    @WithMockUser(username = "user", roles = {"User"})
    public void shouldReturnCurvePointsView() throws Exception {
        mockMvc.perform(get("/curvePoint/list"))
                .andExpect(view().name("curvePoint/list"))
                .andExpect(model().attributeExists("curvePoints"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void shouldReturnCreated() throws Exception {

        mockMvc.perform(post("/curvePoint/validate")
                        .with(csrf())
                        .param("term", "15.0")
                        .param("value", "5.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void shouldReturnTheCurvePointById() throws Exception {

        mockMvc.perform(get("/curvePoint/update/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/update"))
                .andExpect(model().attribute("curvePoint", hasProperty("term", is(1.0))))
                .andExpect(model().attribute("curvePoint", hasProperty("value", is(6.0))));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void shouldReturnRedirectionForUpdating() throws Exception {

        mockMvc.perform(post("/curvePoint/update/{id}", 2)
                        .with(csrf())
                        .param("term", "3.0")
                        .param("value", "55.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));

        CurvePoint updatedCurvePoint = curvePointRepository.findById(2).orElseThrow();
        assertEquals(3.0, updatedCurvePoint.getTerm());
        assertEquals(55.0, updatedCurvePoint.getValue());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void shouldDeleteTheCurvePointSelected() throws Exception {

        mockMvc.perform(get("/curvePoint/delete/{id}", 3))
                .andExpect(status().is3xxRedirection());

        boolean deletedCurvePoint = curvePointRepository.existsById(3);
        assertEquals(false, deletedCurvePoint);
    }

    @Test
    @WithMockUser(username = "user", roles = "User")
    public void shouldReturnUpdateAfterFailedUpdateCurvePointTest() throws Exception {
        mockMvc.perform(post("/curvePoint/update/{id}", 1)
                        .with(csrf())
                        .param("term", "")
                        .param("value", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/update"))
                .andExpect(model().attributeHasFieldErrors("curvePoint", "term", "value"));
    }
}
