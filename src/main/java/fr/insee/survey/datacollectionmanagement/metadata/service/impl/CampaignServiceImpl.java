package fr.insee.survey.datacollectionmanagement.metadata.service.impl;

import fr.insee.survey.datacollectionmanagement.metadata.domain.Campaign;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Partitioning;
import fr.insee.survey.datacollectionmanagement.metadata.dto.CampaignMoogDto;
import fr.insee.survey.datacollectionmanagement.metadata.repository.CampaignRepository;
import fr.insee.survey.datacollectionmanagement.metadata.repository.PartitioningRepository;
import fr.insee.survey.datacollectionmanagement.metadata.service.CampaignService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CampaignServiceImpl implements CampaignService {

    static final Logger LOGGER = LoggerFactory.getLogger(CampaignServiceImpl.class);

    @Autowired
    CampaignRepository campaignRepository;


    public Collection<CampaignMoogDto> getCampaigns() {

        List<CampaignMoogDto> moogCampaigns = new ArrayList<>();
        List<Campaign> campaigns = campaignRepository.findAll();

        for(Campaign campaign:campaigns)
        {
            CampaignMoogDto campaignMoogDto = new CampaignMoogDto();
            campaignMoogDto.setId(campaign.getCampaignId());
            campaignMoogDto.setLabel(campaign.getCampaignWording());

            Optional<Date> dateMin= campaign.getPartitionings().stream().map(Partitioning::getOpeningDate).collect(Collectors.toList()).stream().min(Comparator.comparing(Date::getTime));
            Optional<Date> dateMax= campaign.getPartitionings().stream().map(Partitioning::getOpeningDate).collect(Collectors.toList()).stream().max(Comparator.comparing(Date::getTime));

            campaignMoogDto.setCollectionStartDate(dateMin.get().getTime());
            campaignMoogDto.setCollectionEndDate(dateMax.get().getTime());

            moogCampaigns.add(campaignMoogDto);
        }
        return moogCampaigns;
    }
}