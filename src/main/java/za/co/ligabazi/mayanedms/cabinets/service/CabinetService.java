package za.co.ligabazi.mayanedms.cabinets.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import za.co.ligabazi.mayanedms.cabinets.domain.Cabinet;
import za.co.ligabazi.mayanedms.config.MayanaEDMSConfig;

@Slf4j
@RequiredArgsConstructor
@Service
public class CabinetService {
    private final MayanaEDMSConfig config;
    private final RestTemplate restTemplate;
    
    private final String CONTEXT = "cabinets";

    public Cabinet create(String label, int parentId) {
        String url = String.format("%s/api/%s/%s/", config.getHost(), config.getApiVer(), CONTEXT);
        log.debug("Creating cabinet: {}", label);
    
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); // Adjusted to APPLICATION_JSON for body payload
        headers.setBasicAuth(config.getUsername(), config.getPassword());
    
        Cabinet requestBody = new Cabinet();
        requestBody.setLabel(label);
        requestBody.setParent(parentId);
    
        // Wrap the request body and headers into an HttpEntity
        HttpEntity<Cabinet> requestEntity = new HttpEntity<>(requestBody, headers);
    
        // Use postForEntity (or exchange) to send the POST request
        ResponseEntity<Cabinet> responseEntity = restTemplate.postForEntity(url, requestEntity, Cabinet.class);
        Cabinet response = responseEntity.getBody();
    
        log.debug("Cabinet created: {}", response);
        return response;
    }

    public List<Cabinet> list() throws JsonMappingException, JsonProcessingException, JSONException {
        String url = String.format("%s/api/%s/%s/", config.getHost(), config.getApiVer(), CONTEXT);
        log.debug("Listing cabinets");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setBasicAuth(config.getUsername(), config.getPassword());

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("username", config.getUsername());
        
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Use exchange method to send the request with headers
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        String response = responseEntity.getBody();
        JSONObject json = new JSONObject(response);

        ObjectMapper objectMapper = new ObjectMapper();
        List<Cabinet> cabinets = Arrays.asList(objectMapper.readValue(json.get("results").toString(), Cabinet[].class));

        return cabinets;
    }

    public int addDocumentToCabinet(int documentId, int cabinetId){
        String url = String.format("%s/api/%s/%s/%d/documents/add/", config.getHost(), config.getApiVer(), CONTEXT, cabinetId);
        log.debug("Adding document to cabinet: {}", documentId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(config.getUsername(), config.getPassword());

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("document", documentId+"");

        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        return response.getStatusCode().value();
    }
}
