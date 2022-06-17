package fr.insee.survey.datacollectionmanagement.contact.service;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.contact.domain.Contact;

@Service
public interface ContactService {

    public Contact findByIdentifier(String identifier);

    public List<Contact> findByLastName(String lastName);

    public List<Contact> findByFirstName(String firstName);

    public List<Contact> findByEmail(String email);

    public List<Contact> searchListContactParameters(String identifier, String lastName, String firstName, String email);

}
