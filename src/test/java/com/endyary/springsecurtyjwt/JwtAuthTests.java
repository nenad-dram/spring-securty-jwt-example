package com.endyary.springsecurtyjwt;

import com.endyary.springsecurtyjwt.configuration.JWTUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

class JwtAuthTests extends TestConfiguration {

    @Autowired
    protected JWTUtil jwtUtil;

    @Test
    protected void loginjwt_validRequest_returnJwt() throws Exception {
        String credentialsJson = """
                {
                    "username": "john.doe@mail.com",
                    "password": "Test12345"
                }""";

        MvcResult loginResult = mockMvc.perform(MockMvcRequestBuilders.post("/public/loginjwt")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(credentialsJson))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        Assertions.assertNotNull(loginResult.getResponse().getContentAsString());
    }

    @Test
    protected void getAdminMessage_withJwt_returnOk() throws Exception {
        String jwt = jwtUtil.generateToken("jane.doe@mail.com");

        MvcResult messageResult = mockMvc.perform(MockMvcRequestBuilders.get("/admin/hello")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        Assertions.assertEquals("Hello Admin!", messageResult.getResponse().getContentAsString());
    }

    @Test
    protected void getUserMessage_withAdminJwt_returnUnauthorized() throws Exception {
        String jwt = jwtUtil.generateToken("jane.doe@mail.com");

        mockMvc.perform(MockMvcRequestBuilders.get("/user/hello")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    protected void getUserMessage_afterLogout_returnBadRequest() throws Exception {
        String jwt = jwtUtil.generateToken("john.doe@mail.com");

        mockMvc.perform(MockMvcRequestBuilders.get("/private/logout")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(result -> mockMvc.perform(MockMvcRequestBuilders.get("/user/hello")
                                .header("Authorization", "Bearer " + jwt))
                        .andExpect(MockMvcResultMatchers.status().isBadRequest()));
    }

}
