package fr.insee.survey.datacollectionmanagement.query.service;

import java.util.List;

import fr.insee.survey.datacollectionmanagement.contact.domain.Contact;

public interface SearchContactService {

    List<Contact> searchContactCrossDomain(
        String identifier,
        String lastName,
        String firstName,
        String email,
        String idSu,
        String surveyUnitId,
        String companyName,
        String source,
        String year,
        String period);

}
