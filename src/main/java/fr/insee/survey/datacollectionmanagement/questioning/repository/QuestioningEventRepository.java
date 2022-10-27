package fr.insee.survey.datacollectionmanagement.questioning.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.insee.survey.datacollectionmanagement.questioning.domain.QuestioningEvent;

public interface QuestioningEventRepository extends JpaRepository<QuestioningEvent, Long> {
    
    List<QuestioningEvent> findByQuestioningId(Long questioningId);
}
