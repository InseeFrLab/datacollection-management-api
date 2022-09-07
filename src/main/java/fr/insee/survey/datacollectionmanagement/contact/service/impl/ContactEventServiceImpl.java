package fr.insee.survey.datacollectionmanagement.contact.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.contact.domain.ContactEvent;
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
    public ContactEvent findById(Long id) {
        return contactEventRepository.findById(id).orElseThrow();
    }

    @Override
    public ContactEvent updateContactEvent(ContactEvent contactEvent) {
        return contactEventRepository.save(contactEvent);
    }

    @Override
    public void deleteContactEvent(Long id) {
        contactEventRepository.deleteById(id);
    }

}
