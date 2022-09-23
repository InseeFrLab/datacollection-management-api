package fr.insee.survey.datacollectionmanagement.metadata.dto;

import org.springframework.hateoas.RepresentationModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OwnerDto extends RepresentationModel<OwnerDto>{

    private Long id;
    private String label;

}
