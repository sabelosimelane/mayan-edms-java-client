package za.co.ligabazi.mayanedms.documents.domain;

import java.util.Date;

import lombok.Data;
import za.co.ligabazi.mayanedms.documenttype.domain.DocumentType;

@Data
public class Document {
    private Long id;
    private String label;
    private DocumentType document_type;
    private Date datetime_created;
}
