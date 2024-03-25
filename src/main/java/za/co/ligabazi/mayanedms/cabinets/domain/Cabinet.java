package za.co.ligabazi.mayanedms.cabinets.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Cabinet {
    private Long id;
    private String label;
    private String slug;
    private String url;
    private int parent;
}
