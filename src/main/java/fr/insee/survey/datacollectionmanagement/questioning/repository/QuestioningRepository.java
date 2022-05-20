package fr.insee.survey.datacollectionmanagement.questioning.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.insee.survey.datacollectionmanagement.questioning.domain.Questioning;

public interface QuestioningRepository extends JpaRepository<Questioning, Long> {
}
