package za.co.ligabazi.mayanedms.security;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import za.co.ligabazi.mayanedms.config.MayanaEDMSConfig;

@Service
@Slf4j
@RequiredArgsConstructor
public class Authentication {
    private final MayanaEDMSConfig config;
    private final RestTemplate restTemplate;
    
    public String authenticate() {
        log.info("Authenticating to Mayan EDMS");

        String authUrl = String.format("%s/api/%s/auth/token/obtain/", config.getHost(), config.getApiVer());

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("username", config.getUsername());
        requestBody.put("password", config.getPassword());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(authUrl, request, Map.class);
            Map<String, String> responseBody = response.getBody();
            return responseBody.get("token");
        } catch (HttpClientErrorException ex) {
            log.error("Authentication failed: {}", ex.getResponseBodyAsString());
            throw new RuntimeException("Authentication failed: " + ex.getResponseBodyAsString());
        } catch (Exception ex) {
            log.error("An unexpected error occurred during authentication", ex);
            throw new RuntimeException("An unexpected error occurred during authentication: " + ex.getMessage());
        }
    }
}
