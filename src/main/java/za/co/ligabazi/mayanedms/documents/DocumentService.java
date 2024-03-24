package za.co.ligabazi.mayanedms.documents;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import za.co.ligabazi.mayanedms.config.MayanaEDMSConfig;
import za.co.ligabazi.mayanedms.documents.domain.Document;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentService {
    private final MayanaEDMSConfig config;
    private final RestTemplate restTemplate;

    private final String CONTEXT = "documents";

    public Document uploadDocument(File filePath, Long documentTypeId) {
        String url = String.format("%s/api/%s/%s/upload/", config.getHost(), config.getApiVer(), CONTEXT);
        log.debug("Uploading document: {}", filePath.getAbsolutePath());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setBasicAuth(config.getUsername(), config.getPassword());

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        // File part
        HttpHeaders filePartHeaders = new HttpHeaders();
        filePartHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<FileSystemResource> filePart = new HttpEntity<>(new FileSystemResource(filePath), filePartHeaders);
        body.add("file", filePart);

         Map<String, String> requestBody = new HashMap<>();
        //requestBody.put("document_type_id", uuid.toString());

        HttpHeaders jsonPartHeaders = new HttpHeaders();
        jsonPartHeaders.setContentType(MediaType.APPLICATION_JSON);
        //HttpEntity<String> jsonPart = new HttpEntity<>(beanJson, jsonPartHeaders);
        body.add("document_type_id", documentTypeId);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Document> response = restTemplate.postForEntity(url, requestEntity, Document.class);

        log.debug("Document uploaded: {}", response.getBody());
        return response.getBody();
    }
}
