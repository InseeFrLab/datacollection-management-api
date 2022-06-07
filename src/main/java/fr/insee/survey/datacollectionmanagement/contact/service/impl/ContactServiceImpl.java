package fr.insee.survey.datacollectionmanagement.contact.service.impl;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.contact.domain.Contact;
import fr.insee.survey.datacollectionmanagement.contact.repository.ContactRepository;
import fr.insee.survey.datacollectionmanagement.contact.service.ContactService;

@Service
public class ContactServiceImpl implements ContactService {
	
    @Autowired
    private ContactRepository contactRepository;

	public Contact findById(String id) {
		return contactRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Contact not found"));
	}
	
	public List<Contact> findByEmail(String email) {
		return contactRepository.findByEmail(email);//.orElseThrow(() -> new NoSuchElementException("Contact not found")); 
	}

	public List<Contact> findByLastName(String lastName) {
		return contactRepository.findByLastName(lastName);//.orElseThrow(() -> new NoSuchElementException("Contact not found"));
	}

	public List<Contact> findByFirstName(String firstName) {
		return contactRepository.findByFirstName(firstName);//.orElseThrow(() -> new NoSuchElementException("Contact not found"));
	}

}
