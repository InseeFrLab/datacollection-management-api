package fr.insee.survey.datacollectionmanagement.questioning.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.insee.survey.datacollectionmanagement.questioning.domain.QuestioningAccreditation;

public interface QuestioningAccreditationRepository extends JpaRepository<QuestioningAccreditation, Long> {

    public List<QuestioningAccreditation> findByIdContact(String idContact);

}
