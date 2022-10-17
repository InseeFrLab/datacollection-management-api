package fr.insee.survey.datacollectionmanagement.metadata.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.metadata.domain.Campaign;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Partitioning;
import fr.insee.survey.datacollectionmanagement.metadata.dto.CampaignMoogDto;
import fr.insee.survey.datacollectionmanagement.metadata.repository.CampaignRepository;
import fr.insee.survey.datacollectionmanagement.metadata.service.CampaignService;

@Service
public class CampaignServiceImpl implements CampaignService {

    static final Logger LOGGER = LoggerFactory.getLogger(CampaignServiceImpl.class);

    @Autowired
    CampaignRepository campaignRepository;

    public Collection<CampaignMoogDto> getCampaigns() {

        List<CampaignMoogDto> moogCampaigns = new ArrayList<>();
        List<Campaign> campaigns = campaignRepository.findAll();

        for (Campaign campaign : campaigns) {
            CampaignMoogDto campaignMoogDto = new CampaignMoogDto();
            campaignMoogDto.setId(campaign.getCampaignId());
            campaignMoogDto.setLabel(campaign.getCampaignWording());

            Optional<Date> dateMin =
                campaign.getPartitionings().stream().map(Partitioning::getOpeningDate).collect(Collectors.toList()).stream()
                    .min(Comparator.comparing(Date::getTime));
            Optional<Date> dateMax =
                campaign.getPartitionings().stream().map(Partitioning::getOpeningDate).collect(Collectors.toList()).stream()
                    .max(Comparator.comparing(Date::getTime));

            campaignMoogDto.setCollectionStartDate(dateMin.get().getTime());
            campaignMoogDto.setCollectionEndDate(dateMax.get().getTime());

            moogCampaigns.add(campaignMoogDto);
        }
        return moogCampaigns;
    }

    @Override
    public List<Campaign> findbyPeriod(String period) {
        return campaignRepository.findByPeriod(period);
    }

    @Override
    public Campaign findById(String idCampaign) {
        return campaignRepository.findById(idCampaign).orElseThrow(() -> new NoSuchElementException("Campaign not found"));
    }

    @Override
    public List<Campaign> findbySourceYearPeriod(String source, Integer year, String period) {
        return campaignRepository.findBySourceYearPeriod(source, year, period);
    }
    
    @Override
    public List<Campaign> findbySourcePeriod(String source, String period) {
        return campaignRepository.findBySourcePeriod(source, period);
    }
    
    @Override
    public Page<Campaign> findAll(Pageable pageable) {
        return campaignRepository.findAll(pageable);
    }

    @Override
    public Campaign updateCampaign(Campaign campaign) {
        return campaignRepository.save(campaign);
    }

    @Override
    public void deleteCampaignById(String id) {
        campaignRepository.deleteById(id);
    }
    
}
