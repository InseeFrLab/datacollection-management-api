package fr.insee.survey.datacollectionmanagement.metadata.repository;

import fr.insee.survey.datacollectionmanagement.metadata.domain.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampaignRepository extends JpaRepository<Campaign, String> {
}
