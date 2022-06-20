package fr.insee.survey.datacollectionmanagement.metadata.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.insee.survey.datacollectionmanagement.metadata.domain.Campaign;

public interface CampaignRepository extends JpaRepository<Campaign, String> {

    List<Campaign> findByPeriod(String period);
}
