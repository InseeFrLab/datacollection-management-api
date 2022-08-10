package fr.insee.survey.datacollectionmanagement.query.dto;

import java.io.Serializable;

import fr.insee.survey.datacollectionmanagement.contact.domain.Address;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoogSearchDto implements Serializable {

    private static final long serialVersionUID = 6159952555492309770L;
    private String identifier;
    private String idSu;
    private Address address;
    private String campaign;
    private String firstName;
    private String lastName;

}
