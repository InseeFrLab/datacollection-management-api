package fr.insee.survey.datacollectionmanagement.contact.service.impl;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.contact.domain.Contact;
import fr.insee.survey.datacollectionmanagement.contact.domain.ContactEvent;
import fr.insee.survey.datacollectionmanagement.contact.repository.ContactEventRepository;
import fr.insee.survey.datacollectionmanagement.contact.service.ContactEventService;
import fr.insee.survey.datacollectionmanagement.contact.service.ContactService;

@Service
public class ContactEventServiceImpl implements ContactEventService {

    @Autowired
    private ContactEventRepository contactEventRepository;
    
    @Autowired
    private ContactService contactService;

    @Override
    public Page<ContactEvent> findAll(Pageable pageable) {
        return contactEventRepository.findAll(pageable);
    }

    @Override
    public ContactEvent findById(Long id) {
        return contactEventRepository.findById(id).orElseThrow();
    }

    @Override
    public ContactEvent updateContactEvent(ContactEvent contactEvent) {
        return contactEventRepository.save(contactEvent);
    }

    @Override
    public void deleteContactEvent(Long id) {
        ContactEvent contactEvent = findById(id);
        Contact contact = contactEvent.getContact();
        Set<ContactEvent> setContact = contact.getContactEvents().stream().filter(ce -> ce.getId()!=id).collect(Collectors.toSet());
        contact.setContactEvents(setContact);
        contactService.updateOrCreateContact(contact);
        contactEventRepository.deleteById(id);
    }

}
