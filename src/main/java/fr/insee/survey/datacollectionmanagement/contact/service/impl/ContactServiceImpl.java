package fr.insee.survey.datacollectionmanagement.contact.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.contact.domain.Contact;
import fr.insee.survey.datacollectionmanagement.contact.repository.AddressRepository;
import fr.insee.survey.datacollectionmanagement.contact.repository.ContactRepository;
import fr.insee.survey.datacollectionmanagement.contact.service.ContactService;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public Page<Contact> findAll(Pageable pageable) {
        return contactRepository.findAll(pageable);
    }

    @Override
    public Contact findByIdentifier(String identifier) throws NoSuchElementException {
        return contactRepository.findById(identifier).orElseThrow();
    }

    @Override
    public Contact updateOrCreateContact(Contact contact) {
        return contactRepository.save(contact);
    }

    @Override
    public void deleteContact(String identifier) throws NoSuchElementException {
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
