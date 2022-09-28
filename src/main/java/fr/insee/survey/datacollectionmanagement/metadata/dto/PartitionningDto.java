package fr.insee.survey.datacollectionmanagement.metadata.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartitionningDto {

    private String id;
    private String status;
    private Date openingDate;
    private Date closingDate;
    private Date returnDate;
}
