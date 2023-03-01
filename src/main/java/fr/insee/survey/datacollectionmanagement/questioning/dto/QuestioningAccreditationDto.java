package fr.insee.survey.datacollectionmanagement.questioning.dto;

import java.util.Date;

import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestioningAccreditationDto {

    @Id
    @JsonIgnore
    private Long id;

    private boolean isMain;
    private Date creationDate;
    private String creationAuthor;
    private String idContact;

}
