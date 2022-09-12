package fr.insee.survey.datacollectionmanagement.metadata.dto;

import java.util.Date;

import org.springframework.hateoas.RepresentationModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartitionningDto extends RepresentationModel<PartitionningDto> {

    private String id;
    private String status;
    private Date openingDate;
    private Date closingDate;
    private Date returnDate;
}
