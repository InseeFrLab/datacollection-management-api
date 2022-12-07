package fr.insee.survey.datacollectionmanagement.query.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.query.service.CheckHabilitationService;
import fr.insee.survey.datacollectionmanagement.view.service.ViewService;

@Service
public class CheckHabilitationServiceImpl implements CheckHabilitationService {

    @Autowired
    private ViewService viewService;

    @Override
    public boolean checkHabilitation(String identifier, String idSu, String campaignId) {
        return viewService.countViewByIdentifierIdSuCampaignId(identifier, idSu, campaignId) != 0;
    }

}
