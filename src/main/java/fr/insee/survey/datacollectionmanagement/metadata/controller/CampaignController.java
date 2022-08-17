package fr.insee.survey.datacollectionmanagement.metadata.controller;

import java.util.NoSuchElementException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.insee.survey.datacollectionmanagement.metadata.domain.Campaign;
import fr.insee.survey.datacollectionmanagement.metadata.service.CampaignService;

@RestController
@CrossOrigin
public class CampaignController {

    static final Logger LOGGER = LoggerFactory.getLogger(CampaignController.class);

    @Autowired
    private CampaignService campaignService;

    @GetMapping(value = "campaigns")
    public Page<Campaign> findContacts(
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "20") Integer size,
        @RequestParam(defaultValue = "campaignId") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return campaignService.findAll(pageable);
    }

    @GetMapping(value = "campaigns/{id}")
    public ResponseEntity<?> findContact(@PathVariable("id") String id) {
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
    public ResponseEntity<?> putContact(@PathVariable("id") String id, @RequestBody Campaign campaign) {
        if (StringUtils.isBlank(campaign.getCampaignId()) || !campaign.getCampaignId().equalsIgnoreCase(id)) {
            return new ResponseEntity<>("id and campaign identifier don't match", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(campaignService.updateCampaign(campaign), HttpStatus.OK);
    }


}
