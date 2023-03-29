package fr.insee.survey.datacollectionmanagement.query.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import fr.insee.survey.datacollectionmanagement.config.JSONCollectionWrapper;
import fr.insee.survey.datacollectionmanagement.query.dto.MoogExtractionRowDto;
import fr.insee.survey.datacollectionmanagement.questioning.service.UploadService;
import fr.insee.survey.datacollectionmanagement.questioning.service.QuestioningEventService;
import fr.insee.survey.datacollectionmanagement.questioning.service.QuestioningService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import fr.insee.survey.datacollectionmanagement.constants.Constants;
import fr.insee.survey.datacollectionmanagement.contact.domain.Contact;
import fr.insee.survey.datacollectionmanagement.contact.service.ContactService;
import fr.insee.survey.datacollectionmanagement.query.dto.MoogQuestioningEventDto;
import fr.insee.survey.datacollectionmanagement.query.dto.MoogSearchDto;
import fr.insee.survey.datacollectionmanagement.query.service.MoogService;
import fr.insee.survey.datacollectionmanagement.view.domain.View;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@PreAuthorize("@AuthorizeMethodDecider.isInternalUser() "
        + "|| @AuthorizeMethodDecider.isWebClient() "
        + "|| @AuthorizeMethodDecider.isAdmin() ")
@Tag(name = "5 - Moog", description = "Enpoints for moog")
@Slf4j
public class MoogController {

    static final Logger LOGGER = LoggerFactory.getLogger(MoogController.class);

    @Autowired
    private MoogService moogService;

    @Autowired
    private ContactService contactService;

    @Autowired
    QuestioningEventService questioningEventService;

    @Autowired
    QuestioningService questioningService;

    @Autowired
    UploadService uploadService;

    @GetMapping(path = Constants.API_MOOG_SEARCH)
    public ResponseEntity<?> moogSearch(@RequestParam(required = false) String filter1,
            @RequestParam(required = false) String filter2,
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @RequestParam(defaultValue = "20", required = false) int pageSize) {

        List<View> listView = moogService.moogSearch(filter1);

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        int start = (int) pageable.getOffset();
        int end = (int) ((start + pageable.getPageSize()) > listView.size() ? listView.size()
                : (start + pageable.getPageSize()));

        if (start <= end) {
            Page<MoogSearchDto> page = new PageImpl<MoogSearchDto>(
                    moogService.transformListViewToListMoogSearchDto(listView.subList(start, end)), pageable,
                    listView.size());
            return new ResponseEntity<>(page, HttpStatus.OK);

        } else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

    @GetMapping(path = Constants.API_MOOG_MAIL, produces = "application/json")
    @Operation(summary = "Get Moog questioning events by campaign and idSu")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<?> getMoogMail(@PathVariable("id") String contactId) {
        Optional<Contact> contact = contactService.findByIdentifier(contactId);
        return contact.isPresent() ? ResponseEntity.ok().body(contact.get().getEmail())
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Contact not found");
    }

    @GetMapping(path = Constants.API_MOOG_EVENTS, produces = "application/json")
    @Operation(summary = "Get Moog questioning events by campaign and idSu")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = MoogQuestioningEventDto.class)))),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    public ResponseEntity<?> getMoogQuestioningEvents(@PathVariable("campaign") String campaignId,
            @PathVariable("id") String idSu) {
        return new ResponseEntity<>(Map.of("datas", moogService.getMoogEvents(campaignId, idSu)), HttpStatus.OK);

    }

    @GetMapping(value = Constants.MOOG_API_CAMPAIGN_EXTRACTION, produces = "application/json")
    public JSONCollectionWrapper<MoogExtractionRowDto> provideDataForExtraction(@PathVariable String idCampaign) {
        LOGGER.info("Request GET for extraction of campaign : {}", idCampaign);
        return moogService.getExtraction(idCampaign);
    }

    @GetMapping(value = Constants.MOOG_API_CAMPAIGN_SURVEYUNITS_FOLLOWUP, produces = "application/json")
    public JSONCollectionWrapper<MoogExtractionRowDto> displaySurveyUnitsToFollowUp(@PathVariable String idCampaign) {
        LOGGER.info("Request GET for su to follow up - campaign {}", idCampaign);
        return new JSONCollectionWrapper<MoogExtractionRowDto>(moogService.getSurveyUnitsToFollowUp(idCampaign));
    }


}
