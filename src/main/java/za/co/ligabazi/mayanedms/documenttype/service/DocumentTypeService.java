package za.co.ligabazi.mayanedms.documenttype.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
import za.co.ligabazi.mayanedms.documenttype.domain.DocumentType;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentTypeService {
    private final MayanaEDMSConfig config;
    private final RestTemplate restTemplate;

    private final String CONTEXT = "document_types";
    
    public DocumentType create(String label){
        UUID uuid = UUID.randomUUID();

        String url = String.format("%s/api/%s/%s/", config.getHost(), config.getApiVer(), CONTEXT);
        log.debug("Creating document type: {}", label);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("document_type_id", uuid.toString());
        requestBody.put("label", label);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBasicAuth(config.getUsername(), config.getPassword());

        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<DocumentType> response = restTemplate.postForEntity(url, request, DocumentType.class);
            DocumentType responseBody = response.getBody();
            return responseBody;
        } catch (HttpClientErrorException ex) {
            log.error("Authentication failed: {}", ex.getResponseBodyAsString());
            throw new RuntimeException("Authentication failed: " + ex.getResponseBodyAsString());
        } catch (Exception ex) {
            log.error("An unexpected error occurred during authentication", ex);
            throw new RuntimeException("An unexpected error occurred during authentication: " + ex.getMessage());
        }
    }
}
