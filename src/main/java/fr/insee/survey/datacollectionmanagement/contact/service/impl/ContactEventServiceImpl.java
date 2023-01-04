package fr.insee.survey.datacollectionmanagement.contact.service.impl;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

import fr.insee.survey.datacollectionmanagement.contact.domain.Contact;
import fr.insee.survey.datacollectionmanagement.contact.domain.ContactEvent;
import fr.insee.survey.datacollectionmanagement.contact.domain.ContactEvent.ContactEventType;
import fr.insee.survey.datacollectionmanagement.contact.repository.ContactEventRepository;
import fr.insee.survey.datacollectionmanagement.contact.service.ContactEventService;

@Service
public class ContactEventServiceImpl implements ContactEventService {

    @Autowired
    private ContactEventRepository contactEventRepository;

    @Override
    public Page<ContactEvent> findAll(Pageable pageable) {
        return contactEventRepository.findAll(pageable);
    }

    @Override
    public Optional<ContactEvent> findById(Long id) {
        return contactEventRepository.findById(id);
    }

    @Override
    public ContactEvent saveContactEvent(ContactEvent contactEvent) {
        return contactEventRepository.save(contactEvent);
    }

    @Override
    public void deleteContactEvent(Long id) {
        contactEventRepository.deleteById(id);
    }

    @Override
    public Set<ContactEvent> findContactEventsByContact(Contact contact) {
        return contactEventRepository.findByContact(contact);
    }

    @Override
    public ContactEvent createContactEvent(Contact contact, ContactEventType type, JsonNode payload) {
        ContactEvent contactEventCreate = new ContactEvent();
        contactEventCreate.setContact(contact);
        contactEventCreate.setType(type);
        contactEventCreate.setPayload(payload);
        contactEventCreate.setEventDate(new Date());
        return contactEventCreate;
    }
}
