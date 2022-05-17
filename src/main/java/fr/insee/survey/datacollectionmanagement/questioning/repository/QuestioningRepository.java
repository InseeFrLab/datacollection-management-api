package fr.insee.survey.datacollectionmanagement.questioning.repository;

import fr.insee.survey.datacollectionmanagement.metadata.domain.CampaignEvent;
import fr.insee.survey.datacollectionmanagement.questioning.domain.Questioning;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestioningRepository extends JpaRepository<Questioning, Long> {
}
