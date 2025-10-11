package com.nnk.springboot.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
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
public class BidListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BidListRepository bidListRepository;

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void shouldReturnCreated() throws Exception {

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
    public void shouldReturnTheBidListById() throws Exception {

        mockMvc.perform(get("/bidList/update/{id}", 3))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/update"))
                .andExpect(model().attribute("bidList", hasProperty("account", is("lastAccount"))))
                .andExpect(model().attribute("bidList", hasProperty("type", is("number3"))))
                .andExpect(model().attribute("bidList", hasProperty("bidQuantity", is(400.0))));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void shouldReturnRedirectionForUpdating() throws Exception {

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
    public void shouldDeleteTheBidListSelected() throws Exception {

        mockMvc.perform(get("/bidList/delete/{id}", 3))
                .andExpect(status().is3xxRedirection());

        boolean deletedBidList = bidListRepository.existsById(3);
        assertEquals(false, deletedBidList);
    }

    @Test
    @WithMockUser(username = "user", roles = "User")
    public void shouldReturnUpdateAfterFailedUpdateBidTest() throws Exception {
        mockMvc.perform(post("/bidList/update/{id}", 1)
                        .with(csrf())
                        .param("account", "")
                        .param("type", "")
                        .param("bidQuantity", ""))
                .andExpect(view().name("bidList/list"))
                .andExpect(model().attributeHasFieldErrors("bidList", "account", "type", "bidQuantity"));
    }
}


