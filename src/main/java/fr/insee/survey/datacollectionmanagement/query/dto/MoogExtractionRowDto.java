package fr.insee.survey.datacollectionmanagement.query.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class MoogExtractionRowDto {

    private String status;
    private String dateInfo;
    private String idSu;
    private String idContact;
    private String lastname;
    private String firstname;
    private String address;
    private String batchNumber;
    private int pnd;

}
