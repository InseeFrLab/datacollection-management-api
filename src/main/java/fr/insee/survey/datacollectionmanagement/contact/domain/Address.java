package fr.insee.survey.datacollectionmanagement.contact.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Address {

    @Id @GeneratedValue
    private Long id;

    private String streetNumber;
    private String repetitionIndex;
    private String streetType;
    private String streetName;
    private String addressSupplement;
    private String cityName;
    private String zipCode;
    private String cedexCode;
    private String cedexName;
    private String specialDistribution;
    private String countryCode;
    private String countryName;

    public String toStringMoog() {
        return (streetNumber!=null?streetNumber:"") +
                " "+ (repetitionIndex!=null?repetitionIndex:"")+
                " " +(streetType!=null?streetType:"")+
                "" + (streetName!=null?streetName:"")+
                " " +(addressSupplement!=null?addressSupplement:"") +
                " " + (cityName!=null?cityName:"")+
                " " + (zipCode!=null?zipCode:"") +
                " " + (cedexCode!=null?cedexCode:"") +
                " " + (cedexName!=null?cedexName:"") +
                " " + (specialDistribution!=null?specialDistribution:"")  +
                " " + (countryCode!=null?countryCode:"") +
                " " + (countryName!=null?countryName:"");
    }
}
