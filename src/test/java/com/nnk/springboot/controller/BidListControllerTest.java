package com.nnk.springboot.controller;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.DataSource;

import java.sql.Connection;
import java.util.Arrays;

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
class BidListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BidListRepository bidListRepository;

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void verifyDatabase() throws Exception {
        System.out.println("Active profile: " + Arrays.toString(
                applicationContext.getEnvironment().getActiveProfiles()));

        // Vérifier la source de données
        DataSource dataSource = applicationContext.getBean(DataSource.class);
        try (Connection conn = dataSource.getConnection()) {
            System.out.println("Database URL: " + conn.getMetaData().getURL());
        }
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void shouldReturnCreated() throws Exception {

        mockMvc.perform(post("/bidList/validate")
                        .with(csrf())
                        .param("account", "newAccount")
                        .param("Type", "newType")
                        .param("bidQuantity", "400"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnTheBidListById() throws Exception {

        mockMvc.perform(get("/bidList/update/{id}", 3))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/update"))
                .andExpect(model().attribute("bidList", hasProperty("account", is("lastAccount"))))
                .andExpect(model().attribute("bidList", hasProperty("type", is("number3"))))
                .andExpect(model().attribute("bidList", hasProperty("bidQuantity", is(400.0))));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnRedirectionForUpdating() throws Exception {

        mockMvc.perform(post("/bidList/update/{id}", 2)
                        .with(csrf())
                        .param("account", "newAccountAgain")
                        .param("type", "newTypeAgain")
                        .param("bidQuantity", "30.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));

        BidList updatedBidList = bidListRepository.findById(2).orElseThrow();
        assertEquals("newAccountAgain", updatedBidList.getAccount());
        assertEquals("newTypeAgain", updatedBidList.getType());
        assertEquals(30.0, updatedBidList.getBidQuantity());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldDeleteTheBidListSelected() throws Exception {

        mockMvc.perform(get("/bidList/delete/{id}", 3))
                .andExpect(status().is3xxRedirection());

        boolean deletedBidList = bidListRepository.existsById(3);
        assertFalse(deletedBidList);
    }

    @Test
    @WithMockUser(username = "user", roles = "User")
    void shouldReturnUpdateAfterFailedUpdateBidTest() throws Exception {
        mockMvc.perform(post("/bidList/update/{id}", 1)
                        .with(csrf())
                        .param("account", "")
                        .param("type", "")
                        .param("bidQuantity", ""))
                .andExpect(view().name("bidList/list"))
                .andExpect(model().attributeHasFieldErrors("bidList", "account", "type", "bidQuantity"));
    }

    @Test
    @WithMockUser(username = "user", roles = "User")
    void shouldReturnTheBidListPage() throws Exception {
        mockMvc.perform(get("/bidList/list"))
                .andExpect(status().isOk())  // HTTP 200
                .andExpect(view().name("bidList/list"))
                .andExpect(model().attributeExists("bidLists"))
                .andExpect(model().attributeExists("username"));
    }
}


