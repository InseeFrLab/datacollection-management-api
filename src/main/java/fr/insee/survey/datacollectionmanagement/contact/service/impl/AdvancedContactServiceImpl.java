package fr.insee.survey.datacollectionmanagement.contact.service.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.contact.domain.Contact;
import fr.insee.survey.datacollectionmanagement.contact.domain.ContactEvent;
import fr.insee.survey.datacollectionmanagement.contact.domain.ContactEvent.ContactEventType;
import fr.insee.survey.datacollectionmanagement.contact.service.AddressService;
import fr.insee.survey.datacollectionmanagement.contact.service.AdvancedContactService;
import fr.insee.survey.datacollectionmanagement.contact.service.ContactEventService;
import fr.insee.survey.datacollectionmanagement.contact.service.ContactService;

@Service
public class AdvancedContactServiceImpl implements AdvancedContactService {

    @Autowired
    private ContactService contactService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private ContactEventService contactEventService;

    @Override
    public Contact createContactAddressEvent(Contact contact) {
        if (contact.getAddress() != null) {
            addressService.saveAddress(contact.getAddress());
        }
        ContactEvent newContactEvent = contactEventService.createContactEvent(contact, ContactEventType.create);
        contact.setContactEvents(new HashSet<>(Arrays.asList(newContactEvent)));
        return contactService.saveContact(contact);
    }

    @Override
    public Contact updateContactAddressEvent(Contact contact) {

        Contact existingContact = contactService.findByIdentifier(contact.getIdentifier());
        if (contact.getAddress() != null) {
            if (existingContact.getAddress() != null) {
                contact.getAddress().setId(existingContact.getAddress().getId());
            }
            addressService.saveAddress(contact.getAddress());
        }

        Set<ContactEvent> setContactEventsContact = contactEventService.findContactEventsByContact(contact);
        ContactEvent contactEventUpdate = contactEventService.createContactEvent(contact, ContactEventType.update);
        setContactEventsContact.add(contactEventUpdate);
        contact.setContactEvents(setContactEventsContact);
        return contactService.saveContact(contact);
    }

    @Override
    public void deleteContactAddressEvent(Contact contact) {
        // delete cascade
        contactService.deleteContact(contact.getIdentifier());

    }

}
