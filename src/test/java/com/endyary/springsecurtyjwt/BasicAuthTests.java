package com.endyary.springsecurtyjwt;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class BasicAuthTests extends TestConfiguration {

    @Test
    protected void getPublicMessage_noAuth_returnOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/public/hello"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    protected void getUserMessage_noAuth_returnBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/hello"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    protected void login_validRequest_returnOk() throws Exception {
        String credentialsJson = """
                {
                    "username": "jane.doe@mail.com",
                    "password": "Test12345"
                }""";

        MvcResult loginResult = mockMvc.perform(MockMvcRequestBuilders.post("/public/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(credentialsJson))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        Assertions.assertEquals("Hello Jane Doe!", loginResult.getResponse().getContentAsString());
    }

    @Test
    protected void getUserMessage_validRequest_returnOk() throws Exception {
        MvcResult messageResult = mockMvc.perform(MockMvcRequestBuilders.get("/user/hello")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("john.doe@mail.com", "Test12345")))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        Assertions.assertEquals("Hello User!", messageResult.getResponse().getContentAsString());
    }
    
}
