package fr.insee.survey.datacollectionmanagement.metadata.dto;

import org.springframework.hateoas.RepresentationModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SourceDto extends RepresentationModel<SourceDto> {

    private String idSource;
    private String longWording;
    private String shortWording;
    private String periodicity;

}
