package fr.insee.survey.datacollectionmanagement.view.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import fr.insee.survey.datacollectionmanagement.view.domain.View;

@Repository
public interface ViewRepository extends PagingAndSortingRepository<View, Long> {

    static final String FIND_DISTNCT_VIEW_BY_IDENTIFIER = "select                                                           "
            + "    distinct on                                                                                              "
            + "    (v.identifier)                                                                                           "
            + "    v.id as id,                                                                                              "
            + "    v.identifier as identifier,                                                                              "
            + "    v.campaign_id as campaign_id,                                                                            "
            + "    v.id_su as id_su                                                                                         "
            + "from                                                                                                         "
            + "    \"view\" v                                                                                               "
            + "where                                                                                                        "
            + "    campaign_id =?1                                                                                         ";   

    View findFirstByIdentifier(String identifier);

    List<View> findByIdentifier(String identifier);
    
    @Query(nativeQuery = true, value=FIND_DISTNCT_VIEW_BY_IDENTIFIER)
    List<View> findDistinctViewByCampaignId(String campaignId);

    List<View> findByIdSu(String idSu);

    List<View> findByIdSuContaining(String field);
  
    Long countViewByIdentifierAndIdSuAndCampaignId(String identifier, String idSu, String campaignId);

    List<View> findByIdentifierContainingAndIdSuNotNull(String identifier);

    void deleteByIdentifier(String identifier);

}
