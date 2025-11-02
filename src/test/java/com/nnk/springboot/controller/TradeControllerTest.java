package com.nnk.springboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
class TradeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TradeRepository tradeRepository;

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Test
    void checkDatabaseUrl() {
        System.out.println(">>> Using database: " + datasourceUrl);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void shouldReturnCreated() throws Exception {

        mockMvc.perform(post("/trade/validate")
                        .with(csrf())
                        .param("account", "newAccount")
                        .param("Type", "newType")
                        .param("buyQuantity", "400"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnTheTradeById() throws Exception {

        mockMvc.perform(get("/trade/update/{id}", 3))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/update"))
                .andExpect(model().attribute("trade", hasProperty("account", is("lastAccount"))))
                .andExpect(model().attribute("trade", hasProperty("type", is("number3"))))
                .andExpect(model().attribute("trade", hasProperty("buyQuantity", is(400.0))));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnRedirectionForUpdating() throws Exception {

        mockMvc.perform(post("/trade/update/{id}", 2)
                        .with(csrf())
                        .param("account", "newAccountAgain")
                        .param("type", "newTypeAgain")
                        .param("buyQuantity", "30.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));

        Trade updatedTrade = tradeRepository.findById(2).orElseThrow();
        assertEquals("newAccountAgain", updatedTrade.getAccount());
        assertEquals("newTypeAgain", updatedTrade.getType());
        assertEquals(30.0, updatedTrade.getBuyQuantity());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldDeleteTheTradeSelected() throws Exception {

        mockMvc.perform(get("/trade/delete/{id}", 3))
                .andExpect(status().is3xxRedirection());

        boolean deleteTrade = tradeRepository.existsById(3);
        assertFalse(deleteTrade);
    }

    @Test
    @WithMockUser(username = "user", roles = "User")
    void shouldReturnUpdateAfterFailedUpdateTrade() throws Exception {
        mockMvc.perform(post("/trade/update/{id}", 1)
                        .with(csrf())
                        .param("account", "")
                        .param("type", ""))
                .andExpect(view().name("trade/list"))
                .andExpect(model().attributeHasFieldErrors("trade", "account", "type"));
    }

    @Test
    @WithMockUser(username = "user", roles = "User")
    void shouldReturnTheTradePage() throws Exception {
        mockMvc.perform(get("/trade/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/list"))
                .andExpect(model().attributeExists("trades"))
                .andExpect(model().attributeExists("username"));
    }

}
