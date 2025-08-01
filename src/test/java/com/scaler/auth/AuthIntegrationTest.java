package com.scaler.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scaler.FhirPatientServiceApplication;
import com.scaler.provider.PatientProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import({PatientProvider.class})
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testLoginAndAccessProtectedEndpoint() throws Exception {
        String username = "admin";
        String password = "admin123";

        var loginRequest = Map.of("username", "admin", "password", "admin123");
        var loginResponse = restTemplate.postForEntity("/auth/login", loginRequest, Map.class);
        String token = (String) loginResponse.getBody().get("token");

        // Authenticated GET
        var headers = new HttpHeaders();
        headers.setBearerAuth(token);
        var entity = new HttpEntity<>(headers);

        var response = restTemplate.exchange("/fhir/Patient", HttpMethod.GET, entity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
