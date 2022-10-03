package fr.insee.survey.datacollectionmanagement.query.dto;

import java.io.Serializable;

import fr.insee.survey.datacollectionmanagement.query.domain.MoogCampaign;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoogSearchDto implements Serializable {

    private static final long serialVersionUID = 6159952555492309770L;
    private String idContact;
    private String idSu;
    private String address;
    private MoogCampaign campaign;
    private String firstName;
    private String lastName;
    private int batchNumber;

}
