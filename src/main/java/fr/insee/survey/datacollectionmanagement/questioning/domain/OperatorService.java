package fr.insee.survey.datacollectionmanagement.questioning.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
@Data
public class OperatorService {

    @Id
    @GeneratedValue
    private Long id;

    private String mail;
    private String countryName;
    private long streetNumber;
    private String streetName;

    @OneToMany
    private Set<Operator> operators;
}
