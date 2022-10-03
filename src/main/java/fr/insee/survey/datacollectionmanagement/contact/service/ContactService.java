package fr.insee.survey.datacollectionmanagement.contact.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.contact.domain.Contact;

@Service
public interface ContactService {

    /**
     * Find all contacts
     * @param pageable
     * @return contact Page
     */
    public Page<Contact> findAll(Pageable pageable);

    /**
     * Find a contact by its identifier.
     * @param identifier
     * @throws NoSuchElementException - if the contact doesn't exist
     * @return contact found
     */
    public Contact findByIdentifier(String identifier) throws NoSuchElementException;

    /**
     * Update an existing contact and its address, or creates a new one
     * @param contact
     * @return contact updated
     */
    public Contact saveContact(Contact contact);


    /**
     * Delete a contact. Delete also the contact address.
     * @throws NoSuchElementException - if the contact doesn't exist
     * @param identifier
     */
    public void deleteContact(String identifier) throws NoSuchElementException;

    public List<Contact> findByLastName(String lastName);

    public List<Contact> findByFirstName(String firstName);

    public List<Contact> findByEmail(String email);

    public List<Contact> searchListContactParameters(String identifier, String lastName, String firstName, String email);
    
    public Contact createContactAddressEvent(Contact contact);

    public Contact updateContactAddressEvent(Contact contact);

    public void deleteContactAddressEvent(Contact contact);


}
