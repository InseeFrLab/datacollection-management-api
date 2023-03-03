package fr.insee.survey.datacollectionmanagement.user.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import java.util.Date;

@Getter
@Setter
public class SourceAccreditationDto {

    @Id
    @JsonIgnore
    private Long id;

    private Date creationDate;
    private String creationAuthor;
    private String idUser;

}
