package fr.insee.survey.datacollectionmanagement.contact.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fr.insee.survey.datacollectionmanagement.contact.domain.Contact;

public interface ContactRepository extends JpaRepository<Contact, String> {
    
    @Query(nativeQuery=true, value="SELECT *  FROM contact ORDER BY random() LIMIT 1")
    public Contact findRandomContact();

	public List<Contact> findByEmail(String mail);

	public List<Contact> findByLastName(String lastName);

	public List<Contact> findByFirstName(String firstName);
	
	}
