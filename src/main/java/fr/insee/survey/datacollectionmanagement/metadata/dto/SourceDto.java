package fr.insee.survey.datacollectionmanagement.metadata.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SourceDto {

    private String idSource;
    private String longWording;
    private String shortWording;
    private String periodicity;

}
