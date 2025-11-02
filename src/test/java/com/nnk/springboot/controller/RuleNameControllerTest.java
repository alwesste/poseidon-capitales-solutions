package com.nnk.springboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
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
class RuleNameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RuleNameRepository ruleNameRepository;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnRuleNameView() throws Exception {
        mockMvc.perform(get("/ruleName/list"))
                .andExpect(view().name("ruleName/list"))
                .andExpect(model().attributeExists("ruleNames"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnCreated() throws Exception {

        mockMvc.perform(post("/ruleName/validate")
                        .with(csrf())
                        .param("name", "test")
                        .param("description", "test")
                        .param("json", "test")
                        .param("template", "test")
                        .param("sqlStr", "test")
                        .param("sqlPart", "test"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnTheRuleNameById() throws Exception {

        mockMvc.perform(get("/ruleName/update/{id}", 2))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/update"))
                .andExpect(model().attribute("ruleName", hasProperty("name", is("leNom"))))
                .andExpect(model().attribute("ruleName", hasProperty("description", is("laDescription"))))
                .andExpect(model().attribute("ruleName", hasProperty("json", is("leJson"))))
                .andExpect(model().attribute("ruleName", hasProperty("template", is("leTemplate"))))
                .andExpect(model().attribute("ruleName", hasProperty("sqlStr", is("leSqlStr"))))
                .andExpect(model().attribute("ruleName", hasProperty("sqlPart", is("leSqlPart"))));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnRedirectionForUpdating() throws Exception {

        mockMvc.perform(post("/ruleName/update/{id}", 2)
                        .with(csrf())
                        .param("name", "leNom")
                        .param("description", "laDescription")
                        .param("json", "leJson")
                        .param("template", "leTemplate")
                        .param("sqlStr", "leSqlStr")
                        .param("sqlPart", "leSqlPart"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));

        RuleName updatedRuleName = ruleNameRepository.findById(2).orElseThrow();
        assertEquals("leNom", updatedRuleName.getName());
        assertEquals("laDescription", updatedRuleName.getDescription());
        assertEquals("leJson", updatedRuleName.getJson());
        assertEquals("leTemplate", updatedRuleName.getTemplate());
        assertEquals("leSqlStr", updatedRuleName.getSqlStr());
        assertEquals("leSqlPart", updatedRuleName.getSqlPart());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldDeleteTheRuleNameSelected() throws Exception {

        mockMvc.perform(get("/ruleName/delete/{id}", 3))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));

        boolean deletedRating = ruleNameRepository.existsById(3);
        assertFalse(deletedRating);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldSendErrorIdIsNotFound() throws Exception {

        mockMvc.perform(get("/ruleName/delete/{id}", 100))
                .andExpect(status().isNotFound());

        boolean deletedRating = ruleNameRepository.existsById(100);
        assertFalse(deletedRating);
    }

    @Test
    @WithMockUser(username = "user", roles = "User")
    void shouldReturnUpdateAfterFailedRuleName() throws Exception {
        mockMvc.perform(post("/ruleName/update/{id}", 1)
                        .with(csrf())
                        .param("name", "")
                        .param("description", "")
                        .param("json", "")
                        .param("template", "")
                        .param("sqlStr", "")
                        .param("sqlPart", ""))
                .andExpect(view().name("ruleName/update"))
                .andExpect(model().attributeHasFieldErrors("ruleName", "name", "description", "json", "template", "sqlStr", "sqlPart"));
    }

    @Test
    @WithMockUser(username = "user", roles = "User")
    void shouldReturnTheRuleNamePage() throws Exception {
        mockMvc.perform(get("/ruleName/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/list"))
                .andExpect(model().attributeExists("ruleNames"))
                .andExpect(model().attributeExists("username"));
    }
}
