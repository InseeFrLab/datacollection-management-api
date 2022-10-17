package fr.insee.survey.datacollectionmanagement.view.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import fr.insee.survey.datacollectionmanagement.view.domain.View;

@Repository
public interface ViewRepository extends PagingAndSortingRepository<View, Long> {

    static final String FIND_DISTNCT_ID = "select                                                "
            + "    distinct(identifier)                                                             "
            + "from                                                                                 "
            + "    public.\"view\"                                                                  "
            + "where                                                                                "
            + "    campaign_id =?1                                                               ";

    View findFirstByIdentifier(String identifier);

    List<View> findByIdentifier(String identifier);
    
    @Query(nativeQuery = true, value=FIND_DISTNCT_ID)
    List<String> findDistinctId(String campaignId);

    List<View> findByIdSu(String idSu);

    List<View> findByIdSuContaining(String field);

    List<View> findViewByIdentifierAndIdSuAndCampaignId(String identifier, String idSu, String campaignId);

    List<View> findByIdentifierContainingAndIdSuNotNull(String identifier);

    void deleteByIdentifier(String identifier);

}
