package fr.insee.survey.datacollectionmanagement.questioning.repository;

import fr.insee.survey.datacollectionmanagement.metadata.domain.CampaignEvent;
import fr.insee.survey.datacollectionmanagement.questioning.domain.QuestioningEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestioningEventRepository extends JpaRepository<QuestioningEvent, String> {
}
