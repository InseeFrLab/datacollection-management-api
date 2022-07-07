package fr.insee.survey.datacollectionmanagement.view.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import fr.insee.survey.datacollectionmanagement.view.domain.View;

@Repository
public interface ViewRepository extends PagingAndSortingRepository<View, Long> {
    
    List<View> findByIdentifier(String identifier);
    
    List<View> findByCampaignId(String campaignId);
    
    List<View> findByIdSu(String idSu);

}
