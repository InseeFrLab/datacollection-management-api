package fr.insee.survey.datacollectionmanagement.query.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.contact.service.AccreditationsCopyService;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Campaign;
import fr.insee.survey.datacollectionmanagement.metadata.service.CampaignService;
import fr.insee.survey.datacollectionmanagement.query.service.CheckAccreditationService;
import fr.insee.survey.datacollectionmanagement.view.service.ViewService;

@Service
public class CheckAccreditationServiceImpl implements CheckAccreditationService {

    @Autowired
    private ViewService viewService;

    @Autowired
    private AccreditationsCopyService accreditationsCopyService;

    @Autowired
    private CampaignService campaignService;

    @Override
    public boolean checkAccreditationV2(String identifier, String idSu, String campaignId) {
        return !viewService.findViewByIdentifierIdSuCampaignId(identifier, idSu, campaignId).isEmpty();
    }

    @Override
    public boolean checkAccreditationV3(String identifier, String idSu, String campaignId) {
        Campaign c = campaignService.findById(campaignId);

        return !accreditationsCopyService.findAccreditationCopy(identifier, idSu, c.getSurvey().getSource().getIdSource(), c.getYear(), c.getPeriod())
            .isEmpty();
    }

}
