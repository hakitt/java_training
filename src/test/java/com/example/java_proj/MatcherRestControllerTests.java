package com.example.java_proj;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MatcherRestControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void checkServerPaths() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().is2xxSuccessful());
        mockMvc.perform(get("/aggSellOrders")).andExpect(status().is2xxSuccessful());
        mockMvc.perform(get("/aggBuyOrders")).andExpect(status().is2xxSuccessful());
        mockMvc.perform(post("/addOrder")).andExpect(status().is4xxClientError());
        mockMvc.perform(post("/orders/")).andExpect(status().is4xxClientError());
    }

    @Test
    public void checkAddAccount() throws Exception {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        Account account = new Account(0,"","John","John");
        Account account2 = new Account(0,"","Harry","Harry");
        String json = ow.writeValueAsString(account);
        String json2 = ow.writeValueAsString(account2);
        mockMvc.perform(post("/addAccount")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(status().is2xxSuccessful());

        mockMvc.perform(post("/addAccount")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json2)).andExpect(content().string("Account username taken"));
    }

    @Test
    public void checkLogin() throws Exception {
        MvcResult result = mockMvc.perform(put("/login")
                        .param("password","Harry")
                        .param("account","Harry"))
                .andReturn();

        String token = result.getResponse().getContentAsString();
        System.out.println("token:" + token);
    }

    @Test
    public void checkAddOrder() throws Exception {
        MvcResult result = mockMvc.perform(put("/login")
                        .param("password","Harry")
                        .param("account","Harry"))
                .andReturn();
        String token = result.getResponse().getContentAsString();

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        Orders order = new Orders(10.0,10.0,"Buy",32,0, "pending");

        String json = ow.writeValueAsString(order);
        mockMvc.perform(post("/addOrder")
                .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)).andExpect(status().is2xxSuccessful());
    }

    @Test
    public void testAddBadOrders() throws Exception{
        MvcResult result = mockMvc.perform(put("/login")
                        .param("password","Harry")
                        .param("account","Harry"))
                .andReturn();
        String token = result.getResponse().getContentAsString();

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        Account account = new Account(0,"","","");
        Orders order = new Orders(-1,10,"Buy",1,1, "pending");
        String json = ow.writeValueAsString(order);
        mockMvc.perform(post("/addOrder")
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        order.setQuantity(10);
        order.setPrice(0);
        json = ow.writeValueAsString(order);
        mockMvc.perform(post("/addOrder")
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        order.setOrderType(null);
        order.setPrice(10);
        json = ow.writeValueAsString(order);
        mockMvc.perform(post("/addOrder")
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

    }
}
