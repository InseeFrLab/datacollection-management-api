package fr.insee.survey.datacollectionmanagement.metadata.dto;

import org.springframework.hateoas.RepresentationModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupportDto extends RepresentationModel<SupportDto> {

    private Long id;
    private String label;
}
