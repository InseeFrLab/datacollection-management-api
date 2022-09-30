package fr.insee.survey.datacollectionmanagement.questioning.domain;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;

@Entity
@Data
public class OperatorService {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String mail;

    @OneToMany
    private Set<Operator> operators;
}
