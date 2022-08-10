package fr.insee.survey.datacollectionmanagement.view.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(indexes = {
    @Index(name = "view_identifier_index", columnList = "identifier"), @Index(name = "view_campaignId_index", columnList = "campaignId"),
    @Index(name = "view_idSu_index", columnList = "idSu")
})
public class View {

    @Id
    @GeneratedValue
    private Long id;
    private String identifier;
    private String campaignId;
    private String idSu;
}
