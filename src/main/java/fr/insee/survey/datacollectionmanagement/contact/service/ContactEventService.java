package fr.insee.survey.datacollectionmanagement.contact.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.contact.domain.ContactEvent;

@Service
public interface ContactEventService {

    public Page<ContactEvent> findAll(Pageable pageable);

    public ContactEvent findById(Long id);

    public ContactEvent updateContactEvent(ContactEvent contactEvent);

    public void deleteContactEvent(Long id);

}
