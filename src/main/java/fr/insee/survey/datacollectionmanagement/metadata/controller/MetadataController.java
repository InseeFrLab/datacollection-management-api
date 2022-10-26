package fr.insee.survey.datacollectionmanagement.metadata.controller;

import fr.insee.survey.datacollectionmanagement.config.JSONCollectionWrapper;
import fr.insee.survey.datacollectionmanagement.metadata.dto.CampaignMoogDto;
import fr.insee.survey.datacollectionmanagement.metadata.service.CampaignService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("@AuthorizeMethodDecider.isInternalUser() "
        + "|| @AuthorizeMethodDecider.isWebClient() ")
public class MetadataController {

    static final Logger LOGGER = LoggerFactory.getLogger(MetadataController.class);

    @Autowired
    CampaignService campaignService;

    @GetMapping(value = "/api/moog/campaigns")
    public JSONCollectionWrapper<CampaignMoogDto> displayCampaignInProgress() {
        LOGGER.info("Request GET campaigns");
        return new JSONCollectionWrapper<CampaignMoogDto>(campaignService.getCampaigns());
    }
}
