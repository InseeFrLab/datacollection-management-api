package fr.insee.survey.datacollectionmanagement.questioning.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Operator {

    @Id
    @GeneratedValue
    private Long id;

    private String firstName;
    private String name;
    private String mail;
    private String phoneNumber;


}
