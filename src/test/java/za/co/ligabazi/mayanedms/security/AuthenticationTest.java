package za.co.ligabazi.mayanedms.security;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.util.Properties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import za.co.ligabazi.mayanedms.config.MayanaEDMSConfig;
import za.co.ligabazi.mayanedms.util.PropertiesLoader;

@ExtendWith(MockitoExtension.class)
public class AuthenticationTest {
    
    @InjectMocks Authentication service;

    @BeforeEach
    public void setUp() throws IOException {
        Properties props = PropertiesLoader.loadProperties("src/test/resources/application-test.properties");

        MayanaEDMSConfig config = MayanaEDMSConfig.builder()
            .host(props.getProperty("mayana.edms.host"))
            .username(props.getProperty("mayana.edms.username"))
            .password(props.getProperty("mayana.edms.password"))
            .build();
        service = new Authentication(config, new RestTemplate());
    }

    @Test
    public void testAuthenticate() {
        assertNotNull(service.authenticate());
    }
}
