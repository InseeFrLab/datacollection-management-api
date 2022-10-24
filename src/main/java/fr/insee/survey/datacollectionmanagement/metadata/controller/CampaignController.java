package fr.insee.survey.datacollectionmanagement.metadata.controller;

import fr.insee.survey.datacollectionmanagement.metadata.domain.Campaign;
import fr.insee.survey.datacollectionmanagement.metadata.dto.CampaignDto;
import fr.insee.survey.datacollectionmanagement.metadata.service.CampaignService;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@PreAuthorize("@AuthorizeMethodDecider.isInternalUser() "
        + "|| @AuthorizeMethodDecider.isWebClient() ")
public class CampaignController {

    static final Logger LOGGER = LoggerFactory.getLogger(CampaignController.class);

    @Autowired
    private CampaignService campaignService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping(value = "campaigns")
    public Page<CampaignDto> getCampaigns(
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "20") Integer size,
        @RequestParam(defaultValue = "campaignId") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<Campaign> pageC = campaignService.findAll(pageable);
        List<CampaignDto> listCampaignsDto = pageC.stream().map(c -> convertToDto(c)).collect(Collectors.toList());
        return new CampaignPage(listCampaignsDto, pageable, pageC.getTotalElements());
    }

    @GetMapping(value = "campaigns/{id}")
    public ResponseEntity<?> getCampaign(@PathVariable("id") String id) {
        Campaign campaign = null;
        try {
            campaign = campaignService.findById(StringUtils.upperCase(id));
            return new ResponseEntity<>(campaign, HttpStatus.OK);
        }
        catch (NoSuchElementException e) {
            return new ResponseEntity<>(campaign, HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return new ResponseEntity<String>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping(value = "campaigns/{id}")
    public ResponseEntity<?> putCampaign(@PathVariable("id") String id, @RequestBody Campaign campaign) {
        if (StringUtils.isBlank(campaign.getCampaignId()) || !campaign.getCampaignId().equalsIgnoreCase(id)) {
            return new ResponseEntity<>("id and campaign identifier don't match", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(campaignService.updateCampaign(campaign), HttpStatus.OK);
    }

    private CampaignDto convertToDto(Campaign campaign) {
        return modelMapper.map(campaign, CampaignDto.class);
    }

    private Campaign convertToEntity(CampaignDto campaignDto) throws ParseException {
        Campaign campaign = modelMapper.map(campaignDto, Campaign.class);

        return campaign;
    }

    class CampaignPage extends PageImpl<CampaignDto> {

        private static final long serialVersionUID = 656181199902518234L;

        public CampaignPage(List<CampaignDto> content, Pageable pageable, long total) {
            super(content, pageable, total);
        }
    }

}
