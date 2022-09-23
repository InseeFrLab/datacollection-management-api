package fr.insee.survey.datacollectionmanagement.contact.service;

import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.contact.domain.Contact;

@Service
public interface AdvancedContactService {

    public Contact createContactAddressEvent(Contact contact);

    public Contact updateContactAddressEvent(Contact contact);

    public void deleteContactAddressEvent(Contact contact);

}
