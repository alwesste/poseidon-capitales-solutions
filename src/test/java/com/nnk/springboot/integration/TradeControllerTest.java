package com.nnk.springboot.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
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
public class TradeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TradeRepository tradeRepository;

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void shouldReturnCreated() throws Exception {

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
    public void shouldReturnTheTradeById() throws Exception {

        mockMvc.perform(get("/trade/update/{id}", 3))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/update"))
                .andExpect(model().attribute("trade", hasProperty("account", is("lastAccount"))))
                .andExpect(model().attribute("trade", hasProperty("type", is("number3"))))
                .andExpect(model().attribute("trade", hasProperty("buyQuantity", is(400.0))));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void shouldReturnRedirectionForUpdating() throws Exception {

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
    public void shouldDeleteTheTradeSelected() throws Exception {

        mockMvc.perform(get("/trade/delete/{id}", 3))
                .andExpect(status().is3xxRedirection());

        boolean deleteTrade = tradeRepository.existsById(3);
        assertEquals(false, deleteTrade);
    }

    @Test
    @WithMockUser(username = "user", roles = "User")
    public void shouldReturnUpdateAfterFailedUpdateTrade() throws Exception {
        mockMvc.perform(post("/trade/update/{id}", 1)
                        .with(csrf())
                        .param("account", "")
                        .param("type", ""))
                .andExpect(view().name("trade/list"))
                .andExpect(model().attributeHasFieldErrors("trade", "account", "type"));
    }

}
