package fr.insee.survey.datacollectionmanagement.contact.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import fr.insee.survey.datacollectionmanagement.contact.domain.Contact;

public interface ContactRepository extends PagingAndSortingRepository<Contact, String> {

    @Query(nativeQuery = true, value = "SELECT *  FROM contact ORDER BY random() LIMIT 1")
    public Contact findRandomContact();

    @Query(nativeQuery = true, value = "SELECT identifier FROM contact TABLESAMPLE system_rows(1);")
    public String findRandomIdentifierContact();

    public List<Contact> findByLastNameIgnoreCase(String lastName);

    public List<Contact> findByFirstNameIgnoreCase(String firstName);

    public List<Contact> findByEmailIgnoreCase(String email);

}
