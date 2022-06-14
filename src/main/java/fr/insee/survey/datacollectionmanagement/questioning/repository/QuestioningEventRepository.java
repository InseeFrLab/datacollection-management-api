package fr.insee.survey.datacollectionmanagement.questioning.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.insee.survey.datacollectionmanagement.questioning.domain.QuestioningEvent;

public interface QuestioningEventRepository extends JpaRepository<QuestioningEvent, Long> {
}
