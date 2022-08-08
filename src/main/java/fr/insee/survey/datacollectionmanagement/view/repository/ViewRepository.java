package fr.insee.survey.datacollectionmanagement.view.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import fr.insee.survey.datacollectionmanagement.view.domain.View;

@Repository
public interface ViewRepository extends PagingAndSortingRepository<View, Long> {
    
    @Query(value = "select * from \"view\" v where v.identifier = ? limit 1", nativeQuery = true)
    List<View> findByIdentifier(String identifier);
    
    List<View> findByCampaignId(String campaignId);
    
    List<View> findByIdSu(String idSu);

}
