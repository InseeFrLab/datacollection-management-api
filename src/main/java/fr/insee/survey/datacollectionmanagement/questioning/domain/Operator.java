package fr.insee.survey.datacollectionmanagement.questioning.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import io.swagger.v3.oas.annotations.media.Schema;

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
