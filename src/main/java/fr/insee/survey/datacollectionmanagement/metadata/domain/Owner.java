package fr.insee.survey.datacollectionmanagement.metadata.domain;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
public class Owner {

    @Id
    private String id;

    private String label;
    private String ministry;
    private String logo;

    @OneToMany
    @EqualsAndHashCode.Exclude
    private Set<Source> sources;

}