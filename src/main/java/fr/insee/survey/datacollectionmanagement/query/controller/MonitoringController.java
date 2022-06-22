package fr.insee.survey.datacollectionmanagement.query.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fr.insee.survey.datacollectionmanagement.config.JSONCollectionWrapper;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Campaign;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Partitioning;
import fr.insee.survey.datacollectionmanagement.metadata.service.CampaignService;
import fr.insee.survey.datacollectionmanagement.metadata.service.PartitioningService;
import fr.insee.survey.datacollectionmanagement.query.dto.MoogFollowUpDto;
import fr.insee.survey.datacollectionmanagement.query.dto.MoogProgressDto;
import fr.insee.survey.datacollectionmanagement.query.service.MonitoringService;
import fr.insee.survey.datacollectionmanagement.questioning.service.QuestioningService;


@RestController
@CrossOrigin
public class MonitoringController {
    static final Logger LOGGER = LoggerFactory.getLogger(MonitoringController.class);

    @Autowired
    MonitoringService monitoringService;

   /* @Autowired
    QuestioningEventService questioningEventService;*/

    @Autowired
    QuestioningService questioningService;

    @Autowired
    PartitioningService partitioningService;

    @Autowired
    CampaignService campaignService;


    @GetMapping(value = "/temp/moog/campaigns/{idCampaign}/monitoring/progress", produces = "application/json")
    public JSONCollectionWrapper<MoogProgressDto> getDataForProgress(@PathVariable String idCampaign) {
        LOGGER.info("Request GET for monitoring moog progress table for campaign : {}", idCampaign);
        return monitoringService.getProgress(idCampaign);
    }

    @GetMapping(value = "/temp/moog/campaigns/{idCampaign}/monitoring/follow-up", produces = "application/json")
    public JSONCollectionWrapper<MoogFollowUpDto> getDataToFollowUp(@PathVariable String idCampaign) {
        LOGGER.info("Request GET for following table for campaign : {}", idCampaign);
        return monitoringService.getFollowUp(idCampaign);
    }

    @GetMapping(value = "/moog/campaigns/{idCampaign}/monitoring/progress", produces = "application/json")
    public JSONCollectionWrapper<MoogProgressDto> getDataForProgressTemp(@PathVariable String idCampaign) {
        LOGGER.info("Request GET for monitoring moog progress table for campaign : {}", idCampaign);
        List<MoogProgressDto> moogProgressCampaign = new ArrayList<>();
        Campaign campaign = campaignService.findById(idCampaign);
        LOGGER.info("{} partitionings found", campaign.getPartitionings().stream().map(Partitioning::getId).collect(Collectors.toList()).size());
        campaign.getPartitionings().forEach(part -> LOGGER.info("{} partitionig found", part.getId()));

      /*  for(Partitioning part:campaign.getPartitionings()){
            moogProgressCampaign.add(questioningService.getMoogProgressByPartioning(part.getId()));
        }*/

        //return new JSONCollectionWrapper<>(moogProgressCampaign);
        return null;
    }

    @GetMapping(value = "/moog/campaigns/{idCampaign}/monitoring/follow-up", produces = "application/json")
    public JSONCollectionWrapper<MoogFollowUpDto> getDataToFollowUpTemp(@PathVariable String idCampaign) {
        LOGGER.info("Request GET for following table for campaign : {}", idCampaign);
        return monitoringService.getFollowUp(idCampaign);
    }
}
