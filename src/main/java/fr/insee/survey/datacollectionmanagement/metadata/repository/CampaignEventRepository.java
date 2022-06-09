package fr.insee.survey.datacollectionmanagement.metadata.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.insee.survey.datacollectionmanagement.metadata.domain.CampaignEvent;

public interface CampaignEventRepository extends JpaRepository<CampaignEvent, Long> {
}
