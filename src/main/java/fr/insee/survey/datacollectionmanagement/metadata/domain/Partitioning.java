package fr.insee.survey.datacollectionmanagement.metadata.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(indexes = {
        @Index(name = "campainId_index", columnList = "campaign_id")
})
public class Partitioning {

    @Id
    private String id;
    private String label;
    private Date openingDate;
    private Date closingDate;
    private Date returnDate;

    @OneToOne
    private Campaign campaign;

}
