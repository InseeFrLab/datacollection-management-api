package fr.insee.survey.datacollectionmanagement.contact.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

import fr.insee.survey.datacollectionmanagement.contact.domain.Contact;
import fr.insee.survey.datacollectionmanagement.contact.domain.ContactEvent;
import fr.insee.survey.datacollectionmanagement.contact.domain.ContactEvent.ContactEventType;

@Service
public interface ContactEventService {

    public Page<ContactEvent> findAll(Pageable pageable);

    public Optional<ContactEvent> findById(Long id);

    public ContactEvent saveContactEvent(ContactEvent contactEvent);

    public void deleteContactEvent(Long id);
    
    public Set<ContactEvent> findContactEventsByContact (Contact contact);

    ContactEvent createContactEvent(Contact contact, ContactEventType type, JsonNode payload);

}
