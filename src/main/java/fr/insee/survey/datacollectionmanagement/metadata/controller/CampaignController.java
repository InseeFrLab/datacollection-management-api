package fr.insee.survey.datacollectionmanagement.metadata.controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import fr.insee.survey.datacollectionmanagement.questioning.domain.Upload;
import fr.insee.survey.datacollectionmanagement.questioning.service.UploadService;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import fr.insee.survey.datacollectionmanagement.constants.Constants;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Campaign;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Partitioning;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Survey;
import fr.insee.survey.datacollectionmanagement.metadata.dto.CampaignDto;
import fr.insee.survey.datacollectionmanagement.metadata.service.CampaignService;
import fr.insee.survey.datacollectionmanagement.metadata.service.SurveyService;
import fr.insee.survey.datacollectionmanagement.questioning.service.QuestioningService;
import fr.insee.survey.datacollectionmanagement.view.service.ViewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@PreAuthorize("@AuthorizeMethodDecider.isInternalUser() "
        + "|| @AuthorizeMethodDecider.isWebClient() "
        + "|| @AuthorizeMethodDecider.isAdmin() ")
@Tag(name = "3 - Metadata", description = "Enpoints to create, update, delete and find entities in metadata domain")
@Slf4j
public class CampaignController {

    @Autowired
    private CampaignService campaignService;

    @Autowired
    private SurveyService surveyService;

    @Autowired
    private ViewService viewService;

    @Autowired
    private ModelMapper modelmapper;

    @Autowired
    private QuestioningService questioningService;

    @Autowired
    UploadService uploadService;
    
    @Operation(summary = "Search for campaigns, paginated")
    @GetMapping(value = Constants.API_CAMPAIGNS, produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = CampaignPage.class)))
    })
    public ResponseEntity<?> getSources(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(defaultValue = "id") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<Campaign> pageCampaign = campaignService.findAll(pageable);
        List<CampaignDto> listCampaigns = pageCampaign.stream().map(c -> convertToDto(c)).collect(Collectors.toList());
        return ResponseEntity.ok().body(new CampaignPage(listCampaigns, pageable, pageCampaign.getTotalElements()));
    }

    @Operation(summary = "Search for campaigns by the survey id")
    @GetMapping(value = Constants.API_SURVEYS_ID_CAMPAIGNS, produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CampaignDto.class)))),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> getCampaignsBySurvey(@PathVariable("id") String id) {
        try {
            Optional<Survey> survey = surveyService.findById(id);
            if (!survey.isPresent()) {
                log.warn("Survey {} does not exist", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("survey does not exist");
            }
            return ResponseEntity.ok()
                    .body(survey.get().getCampaigns().stream().map(s -> convertToDto(s)).collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error");
        }
    }

    @Operation(summary = "Search for a campaign by its id")
    @GetMapping(value = Constants.API_CAMPAIGNS_ID, produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = CampaignDto.class))),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> getCampaign(@PathVariable("id") String id) {
        try {
            Optional<Campaign> campaign = campaignService.findById(StringUtils.upperCase(id));
            if (!campaign.isPresent()) {
                log.warn("campaign {} does not exist", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("campaign does not exist");
            }
            return ResponseEntity.ok().body(convertToDto(campaign.get()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error");
        }

    }

    @Operation(summary = "Update or create a campaign")
    @PutMapping(value = Constants.API_CAMPAIGNS_ID, produces = "application/json", consumes = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = CampaignDto.class))),
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(schema = @Schema(implementation = CampaignDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> putCampaign(@PathVariable("id") String id, @RequestBody CampaignDto campaignDto) {
        if (StringUtils.isBlank(campaignDto.getId()) || !campaignDto.getId().equalsIgnoreCase(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("id and idCampaign don't match");
        }
        Campaign campaign;
        if (!surveyService.findById(campaignDto.getSurveyId()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Survey does not exist");
        }
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(HttpHeaders.LOCATION,
                ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(campaignDto.getId()).toUriString());
        HttpStatus httpStatus;

        if (campaignService.findById(id).isPresent()) {
            log.info("Update campaign with the id {}", campaignDto.getId());
            campaignService.findById(id);
            httpStatus = HttpStatus.OK;
        } else {
            log.info("Create campaign with the id {}", campaignDto.getId());
            httpStatus = HttpStatus.CREATED;
        }

        campaign = campaignService.insertOrUpdateCampaign(convertToEntity(campaignDto));
        Survey survey = campaign.getSurvey();
        survey.getCampaigns().add(campaign);
        surveyService.insertOrUpdateSurvey(survey);
        return ResponseEntity.status(httpStatus).headers(responseHeaders).body(convertToDto(campaign));
    }

    @Operation(summary = "Delete a campaign, its campaigns, partitionings, questionings ...")
    @DeleteMapping(value = {Constants.API_CAMPAIGNS_ID, Constants.MOOG_API_CAMPAIGNS_ID})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @Transactional
    public ResponseEntity<?> deleteCampaign(@PathVariable("id") String id) {
        try {
            Optional<Campaign> campaign = campaignService.findById(id);
            if (!campaign.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Campaign does not exist");
            }

            int nbQuestioningDeleted = 0;
            Survey survey = campaign.get().getSurvey();
            survey.getCampaigns().remove(campaign.get());
            surveyService.insertOrUpdateSurvey(survey);
            List<Upload> uploadsCamp = uploadService.findAllByIdCampaign(id);
            campaignService.deleteCampaignById(id);
            Set<Partitioning> listPartitionings = campaign.get().getPartitionings();

            int nbViewDeleted = viewService.deleteViewsOfOneCampaign(campaign.get());

            for (Partitioning partitioning : listPartitionings) {
                nbQuestioningDeleted += questioningService.deleteQuestioningsOfOnePartitioning(partitioning);
            }
            uploadsCamp.stream().forEach(up->uploadService.delete(up));
            log.info("Campaign {} deleted with all its metadata children - {} questioning deleted - {} view deleted - {} uploads deleted",
                    id,
                    nbQuestioningDeleted, nbViewDeleted, uploadsCamp.size());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Campaign deleted");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error");
        }
    }

    private CampaignDto convertToDto(Campaign campaign) {
        return modelmapper.map(campaign, CampaignDto.class);
    }

    private Campaign convertToEntity(CampaignDto campaignDto) {
        return modelmapper.map(campaignDto, Campaign.class);
    }

    class CampaignPage extends PageImpl<CampaignDto> {

        public CampaignPage(List<CampaignDto> content, Pageable pageable, long total) {
            super(content, pageable, total);
        }
    }

}
