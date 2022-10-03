package fr.insee.survey.datacollectionmanagement.view.service;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.view.domain.View;

@Service
public interface ViewService {

    View saveView(View view);

    List<View> findViewByIdentifier(String identifier);
    
    View findFirstViewByIdentifier(String identifier);

    List<View> findViewByCampaignId(String campaignId);

    List<View> findViewByIdSu(String idSu);

    List<View> findViewByIdentifierIdSuCampaignId(String identifier, String idSu, String campaignId);

    List<View> findByIdentifierContainingAndIdSuNotNull(String identifier);

    List<View> findViewByIdSuContaining(String field);
    
    View createView(String identifier, String idSu, String campaignId);

    void deleteView(View view);

    void deleteViewByIdentifier(String identifier);

   
}
