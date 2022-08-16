package fr.insee.survey.datacollectionmanagement.contact.controller;

import java.util.NoSuchElementException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.insee.survey.datacollectionmanagement.contact.domain.Contact;
import fr.insee.survey.datacollectionmanagement.contact.service.ContactService;

@RestController
@CrossOrigin
public class ContactController {

    static final Logger LOGGER = LoggerFactory.getLogger(ContactController.class);

    @Autowired
    private ContactService contactService;

    @GetMapping(value = "contacts")
    public Page<Contact> findContacts(
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "20") Integer size,
        @RequestParam(defaultValue = "identifier") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return contactService.findAll(pageable);
    }

    @GetMapping(value = "contacts/{id}")
    public ResponseEntity<?> findContact(@PathVariable("id") String id) {
        Contact contact = null;
        try {
            contact = contactService.findByIdentifier(StringUtils.upperCase(id));
            return new ResponseEntity<>(contact, HttpStatus.OK);
        }
        catch (NoSuchElementException e) {
            return new ResponseEntity<>(contact, HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return new ResponseEntity<String>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping(value = "contacts/{id}")
    public ResponseEntity<?> putContact(@PathVariable("id") String id, @RequestBody Contact contact) {
        if (StringUtils.isBlank(contact.getIdentifier()) || !contact.getIdentifier().equalsIgnoreCase(id)) {
            return new ResponseEntity<>("id and contact identifier don't match", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(contactService.updateContact(contact), HttpStatus.OK);
    }

    @DeleteMapping(value = "contacts/{id}")
    public ResponseEntity<?> deleteContact(@PathVariable("id") String id) {
        try {
            contactService.deleteContact(id);
            return new ResponseEntity<>("Contact deleted", HttpStatus.OK);
        }
        catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>("Contact not found", HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return new ResponseEntity<String>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
