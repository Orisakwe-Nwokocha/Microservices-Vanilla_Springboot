package dev.orisha.user_service.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.orisha.user_service.dto.requests.LoginRequest;
import dev.orisha.user_service.dto.requests.RegisterRequest;
import dev.orisha.user_service.dto.responses.ApiResponse;
import dev.orisha.user_service.dto.responses.LoginResponse;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static dev.orisha.user_service.data.constants.Authority.USER;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = {"/db/data.sql"})
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ModelMapper modelMapper;

    private static final String BLACKLISTED_TOKEN = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJvcmlzaGEuZGV2IiwiaWF0IjoxNzIzMzk0Mjk5LCJleHAiOjE3MjM0ODA2OTksInN1YiI6InVzZXIiLCJwcmluY2lwYWwiOiJ1c2VyIiwiY3JlZGVudGlhbHMiOiJbUFJPVEVDVEVEXSIsImF1dGhvcml0aWVzIjpbIlVTRVIiXX0.E-wHrx_7sp2xSloSMoVuVCNY5OdZ6Wh80BomoSAH8XSWSSrD8WB52EInr6Pc6HQKc6ZLzegGY7kDbqxV3ipwtQ";

    @Test
    void registerUserTest() throws Exception {
        RegisterRequest request = buildRegisterRequest();
        byte[] content = new ObjectMapper().writeValueAsBytes(request);
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isCreated())
                .andDo(print());

    }

    @Test
    public void authenticateUserTest() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("user");
        request.setPassword("password");
        byte[] content = new ObjectMapper().writeValueAsBytes(request);
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void authenticateUserTest_FailsForInvalidCredentials() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("user");
        request.setPassword("wrongPassword");
        byte[] content = new ObjectMapper().writeValueAsBytes(request);
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    public void logoutUserTest() throws Exception {
        String token = getToken();
        mockMvc.perform(post("/api/v1/auth/logout")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    public void testThatBlacklistedTokenCannotBeAuthorized() throws Exception {
        mockMvc.perform(get("/api/v1/user")
                        .header("Authorization", "Bearer " + BLACKLISTED_TOKEN))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    private String getToken() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("admin");
        request.setPassword("password");
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] content = objectMapper.writeValueAsBytes(request);
        MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andReturn();
        ApiResponse<?> response = objectMapper.readValue(result.getResponse().getContentAsByteArray(), ApiResponse.class);
        LoginResponse loginResponse = modelMapper.map(response.getData(), LoginResponse.class);
        return loginResponse.getToken();
    }

    public static RegisterRequest buildRegisterRequest() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("username");
        request.setPassword("password");
        request.setAuthority(USER);
        return request;
    }
}