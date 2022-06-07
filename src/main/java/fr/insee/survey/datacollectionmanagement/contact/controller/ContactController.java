package fr.insee.survey.datacollectionmanagement.contact.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import fr.insee.survey.datacollectionmanagement.contact.domain.Contact;
import fr.insee.survey.datacollectionmanagement.contact.repository.ContactRepository;

public class ContactController {

    @Autowired
    private ContactRepository contactRepository;
	
    @GetMapping(value = "contacts/search?idec={id}")
    public Contact findById(@PathVariable("id") String id){
    	return contactRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Contact not found"));
    }
    
    @GetMapping(value = "contacts/search?lastName={lastName}")
    public List<Contact> findByLastName(@PathVariable("lastName") String lastName){
    	return contactRepository.findByLastName(lastName); //.orElseThrow(() -> new NoSuchElementException("Contact not found"));
    }

}
