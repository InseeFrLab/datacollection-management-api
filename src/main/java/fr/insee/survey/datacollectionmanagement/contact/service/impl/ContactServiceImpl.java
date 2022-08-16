package fr.insee.survey.datacollectionmanagement.contact.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.contact.domain.Contact;
import fr.insee.survey.datacollectionmanagement.contact.domain.ContactEvent;
import fr.insee.survey.datacollectionmanagement.contact.domain.ContactEvent.ContactEventType;
import fr.insee.survey.datacollectionmanagement.contact.repository.AddressRepository;
import fr.insee.survey.datacollectionmanagement.contact.repository.ContactEventRepository;
import fr.insee.survey.datacollectionmanagement.contact.repository.ContactRepository;
import fr.insee.survey.datacollectionmanagement.contact.service.ContactService;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ContactEventRepository contactEventRepository;

    @Override
    public Page<Contact> findAll(Pageable pageable) {
        return contactRepository.findAll(pageable);
    }

    @Override
    public Contact findByIdentifier(String identifier) {
        return contactRepository.findById(identifier).orElseThrow(() -> new NoSuchElementException("Contact not found"));
    }

    @Override
    public Contact updateContact(Contact contact) {
        Contact c = findByIdentifier(contact.getIdentifier());
        if (c != null && !c.equals(contact)) {
            addressRepository.save(contact.getAddress());
            contactRepository.save(contact);
            Set<ContactEvent> setContactEventsContact = contactEventRepository.findByContact(contact);
            List<ContactEvent> listUpdateContact =
                setContactEventsContact.stream().filter(ce -> ce.getType().equals(ContactEventType.update)).collect(Collectors.toList());
            ContactEvent contactEventUpdate = null;
            if (!listUpdateContact.isEmpty())
                contactEventUpdate = listUpdateContact.get(0);
            else {
                contactEventUpdate = new ContactEvent();
                contactEventUpdate.setContact(contact);
                contactEventUpdate.setType(ContactEventType.update);
            }
            contactEventUpdate.setEventDate(new Date());
            contactEventRepository.save(contactEventUpdate);
            c = contact;
        }
        return c;
    }

    @Override
    public void deleteContact(String identifier) {
        contactRepository.deleteById(identifier);
    }

    @Override
    public List<Contact> findByLastName(String lastName) {
        return contactRepository.findByLastNameIgnoreCase(lastName);
    }

    @Override
    public List<Contact> findByFirstName(String firstName) {
        return contactRepository.findByFirstNameIgnoreCase(firstName);
    }

    @Override
    public List<Contact> findByEmail(String email) {
        return contactRepository.findByEmailIgnoreCase(email);
    }

    @Override
    public List<Contact> searchListContactParameters(String identifier, String lastName, String firstName, String email) {

        List<Contact> listContactContact = new ArrayList<>();
        boolean alwaysEmpty = true;

        if ( !StringUtils.isEmpty(identifier)) {
            listContactContact = Arrays.asList(findByIdentifier(identifier));
            alwaysEmpty = false;
        }

        if ( !StringUtils.isEmpty(lastName)) {
            if (listContactContact.isEmpty() && alwaysEmpty) {
                listContactContact.addAll(findByLastName(lastName));
                alwaysEmpty = false;
            }
            else
                listContactContact = listContactContact.stream().filter(c -> c.getLastName().equalsIgnoreCase(lastName)).collect(Collectors.toList());

        }

        if ( !StringUtils.isEmpty(firstName)) {
            if (listContactContact.isEmpty() && alwaysEmpty) {
                listContactContact.addAll(findByFirstName(firstName));
                alwaysEmpty = false;
            }
            else
                listContactContact = listContactContact.stream().filter(c -> c.getFirstName().equalsIgnoreCase(firstName)).collect(Collectors.toList());
        }

        if ( !StringUtils.isEmpty(email)) {
            if (listContactContact.isEmpty() && alwaysEmpty) {
                listContactContact.addAll(findByEmail(email));
                alwaysEmpty = false;
            }
            else
                listContactContact = listContactContact.stream().filter(c -> c.getEmail().equalsIgnoreCase(email)).collect(Collectors.toList());
        }

        return listContactContact;
    }

    @Override
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
        Pageable pageable) {
        if (StringUtils.isEmpty(year))
            return contactRepository.findContactMultiCriteriaAccreditationsCopy(identifier, lastName, firstName, email, idSu, surveyUnitId, companyName, source,
                period, pageable);
        else
            return contactRepository.findContactMultiCriteriaAccreditationsCopyYear(identifier, lastName, firstName, email, idSu, surveyUnitId, companyName,
                source, Integer.parseInt(year), period, pageable);
    }

}
