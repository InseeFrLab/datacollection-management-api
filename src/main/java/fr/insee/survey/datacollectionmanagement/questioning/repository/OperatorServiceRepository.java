package fr.insee.survey.datacollectionmanagement.questioning.repository;

import fr.insee.survey.datacollectionmanagement.metadata.domain.CampaignEvent;
import fr.insee.survey.datacollectionmanagement.questioning.domain.OperatorService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperatorServiceRepository extends JpaRepository<OperatorService, Long> {
}
