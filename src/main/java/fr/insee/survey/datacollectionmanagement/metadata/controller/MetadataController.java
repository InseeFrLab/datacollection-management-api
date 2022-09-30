package fr.insee.survey.datacollectionmanagement.metadata.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.insee.survey.datacollectionmanagement.config.JSONCollectionWrapper;
import fr.insee.survey.datacollectionmanagement.metadata.dto.CampaignMoogDto;
import fr.insee.survey.datacollectionmanagement.metadata.service.CampaignService;

@RestController
@CrossOrigin
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
