package fr.insee.survey.datacollectionmanagement.contact.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.contact.domain.Contact;

@Service
public interface ContactService {
    
    public Page<Contact> findAll(Pageable pageable);

    public Contact findByIdentifier(String identifier);
    
    public Contact updateContact(Contact contact);

    public List<Contact> findByLastName(String lastName);

    public List<Contact> findByFirstName(String firstName);

    public List<Contact> findByEmail(String email);

    public List<Contact> searchListContactParameters(String identifier, String lastName, String firstName, String email);
    
    public Page<Contact> searchListContactAccreditationsCopy(
        String identifier,
        String lastName,
        String firstName,
        String email,
        String idSu,
        String surveyUnitId,
        String companyName,
        String source,
        String year,
        String period,
        Pageable pageable);

}
