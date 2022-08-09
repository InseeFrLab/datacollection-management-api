package fr.insee.survey.datacollectionmanagement.view.service;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.view.domain.View;

@Service
public interface ViewService {

    List<View> findViewByIdentifier(String identifier);

    List<View> findViewByCampaignId(String campaignId);

    List<View> findViewByIdSu(String idSu);
    
    List<View> findViewByIdentifierIdSuCampaignId(String identifier, String idSu, String campaignId);

}
