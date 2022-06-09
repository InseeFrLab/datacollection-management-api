package fr.insee.survey.datacollectionmanagement.contact.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.insee.survey.datacollectionmanagement.contact.domain.ContactEvent;

public interface ContactEventRepository extends JpaRepository<ContactEvent, String> {
}
