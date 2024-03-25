package za.co.ligabazi.mayanedms.cabinets.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.Properties;

import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import za.co.ligabazi.mayanedms.config.MayanaEDMSConfig;
import za.co.ligabazi.mayanedms.util.PropertiesLoader;

@ExtendWith(MockitoExtension.class)
public class CabinetServiceTest {

    @InjectMocks CabinetService service;

    @BeforeEach
    public void setUp() throws IOException {
        Properties props = PropertiesLoader.loadProperties("src/test/resources/application-test.properties");

        MayanaEDMSConfig config = MayanaEDMSConfig.builder()
            .host(props.getProperty("mayana.edms.host"))
            .username(props.getProperty("mayana.edms.username"))
            .password(props.getProperty("mayana.edms.password"))
            .build();
        service = new CabinetService(config, new RestTemplate());
    }

    @DisplayName("Create Cabinet")
    @Test
    public void testCreate() {
        assertNotNull(service.create("Sabelo", 1));
    }

    @DisplayName("List Cabinets")
    @Test
    public void testList() throws JsonMappingException, JsonProcessingException, JSONException {
        assertTrue(service.list().size() > 0);
        
        service.list().forEach(c->System.out.println(c));
    }

    @DisplayName("Add document to cabinet")
    @Test
    void addDocumentToCabinet() {
        assertEquals(200, service.addDocumentToCabinet(6, 3));
    }
}
