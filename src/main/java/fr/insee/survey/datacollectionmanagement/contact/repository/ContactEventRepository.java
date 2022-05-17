package fr.insee.survey.datacollectionmanagement.contact.repository;

import fr.insee.survey.datacollectionmanagement.contact.domain.ContactEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactEventRepository extends JpaRepository<ContactEvent, String> {
}
