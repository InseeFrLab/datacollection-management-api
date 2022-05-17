package fr.insee.survey.datacollectionmanagement.questioning.repository;

import fr.insee.survey.datacollectionmanagement.metadata.domain.CampaignEvent;
import fr.insee.survey.datacollectionmanagement.questioning.domain.QuestioningAccreditation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestioningAccreditationRepository extends JpaRepository<QuestioningAccreditation, Long> {
}
