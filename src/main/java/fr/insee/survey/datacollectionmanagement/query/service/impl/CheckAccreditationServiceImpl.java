package fr.insee.survey.datacollectionmanagement.query.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.query.service.CheckAccreditationService;
import fr.insee.survey.datacollectionmanagement.view.service.ViewService;

@Service
public class CheckAccreditationServiceImpl implements CheckAccreditationService {

    @Autowired
    private ViewService viewService;

    @Override
    public boolean checkAccreditation(String identifier, String idSu, String campaignId) {
        return !viewService.findViewByIdentifierIdSuCampaignId(identifier, idSu, campaignId).isEmpty();
    }

}
