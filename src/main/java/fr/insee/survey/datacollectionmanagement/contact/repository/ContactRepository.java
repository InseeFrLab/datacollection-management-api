package fr.insee.survey.datacollectionmanagement.contact.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fr.insee.survey.datacollectionmanagement.contact.domain.Contact;

public interface ContactRepository extends JpaRepository<Contact, String> {
    
    @Query(nativeQuery=true, value="SELECT *  FROM contact ORDER BY random() LIMIT 1")
    public Contact findRandomContact();
    
    @Query(nativeQuery=true, value="SELECT identifier FROM contact TABLESAMPLE system_rows(1);")
    public String findRandomIdentifierContact();
}
