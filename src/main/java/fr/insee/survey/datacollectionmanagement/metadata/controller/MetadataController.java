package fr.insee.survey.datacollectionmanagement.metadata.controller;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import fr.insee.survey.datacollectionmanagement.config.JSONCollectionWrapper;
import fr.insee.survey.datacollectionmanagement.constants.Constants;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Campaign;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Owner;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Partitioning;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Source;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Support;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Survey;
import fr.insee.survey.datacollectionmanagement.metadata.dto.CampaignDto;
import fr.insee.survey.datacollectionmanagement.metadata.dto.CampaignMoogDto;
import fr.insee.survey.datacollectionmanagement.metadata.dto.MetadataDto;
import fr.insee.survey.datacollectionmanagement.metadata.dto.OwnerDto;
import fr.insee.survey.datacollectionmanagement.metadata.dto.PartitioningDto;
import fr.insee.survey.datacollectionmanagement.metadata.dto.SourceDto;
import fr.insee.survey.datacollectionmanagement.metadata.dto.SupportDto;
import fr.insee.survey.datacollectionmanagement.metadata.dto.SurveyDto;
import fr.insee.survey.datacollectionmanagement.metadata.service.CampaignService;
import fr.insee.survey.datacollectionmanagement.metadata.service.OwnerService;
import fr.insee.survey.datacollectionmanagement.metadata.service.PartitioningService;
import fr.insee.survey.datacollectionmanagement.metadata.service.SourceService;
import fr.insee.survey.datacollectionmanagement.metadata.service.SupportService;
import fr.insee.survey.datacollectionmanagement.metadata.service.SurveyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;

@RestController
@CrossOrigin
@Log4j2
@Tag(name = "3 - Metadata", description = "Enpoints to create, update, delete and find entities in metadata domain")
public class MetadataController {

    @Autowired
    SourceService sourceService;

    @Autowired
    SurveyService surveyService;

    @Autowired
    CampaignService campaignService;

    @Autowired
    PartitioningService partitioningService;

    @Autowired
    OwnerService ownerService;

    @Autowired
    SupportService supportService;

    @Autowired
    private ModelMapper modelmapper;

    @GetMapping(value = "/api/moog/campaigns")
    public JSONCollectionWrapper<CampaignMoogDto> displayCampaignInProgress() {
        log.info("Request GET campaigns");
        return new JSONCollectionWrapper<CampaignMoogDto>(campaignService.getCampaigns());
    }

    @Operation(summary = "Search for a partitiong and metadata by partitioning id")
    @GetMapping(value = Constants.API_METADATA_ID, produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = MetadataDto.class))),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> getMetadata(@PathVariable("id") String id) {
        MetadataDto metadataDto = new MetadataDto();
        try {
            Optional<Partitioning> part = partitioningService.findById(StringUtils.upperCase(id));
            if (!part.isPresent()) {
                log.warn("partitioning {} does not exist", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("partitioning does not exist");
            }
            metadataDto.setPartitioningDto(convertToDto(part.get()));
            metadataDto.setCampaignDto(convertToDto(part.get().getCampaign()));
            metadataDto.setSurveyDto(convertToDto(part.get().getCampaign().getSurvey()));
            metadataDto.setSourceDto(convertToDto(part.get().getCampaign().getSurvey().getSource()));
            metadataDto.setOwnerDto(convertToDto(part.get().getCampaign().getSurvey().getSource().getOwner()));
            metadataDto.setSupportDto(convertToDto(part.get().getCampaign().getSurvey().getSource().getSupport()));
            return ResponseEntity.ok().body(metadataDto);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error");
        }

    }

    @Operation(summary = "Insert or update a partitiong and metadata by partitioning id")
    @PutMapping(value = Constants.API_METADATA_ID, produces = "application/json", consumes = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = MetadataDto.class))),
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(schema = @Schema(implementation = MetadataDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @Transactional
    public ResponseEntity<?> putMetadata(@PathVariable("id") String id,
            @RequestBody MetadataDto metadataDto) {
        try {
            if (StringUtils.isBlank(metadataDto.getPartitioningDto().getId())
                    || !metadataDto.getPartitioningDto().getId().equalsIgnoreCase(id)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("id and idPartitioning don't match");
            }
            MetadataDto metadataReturn = new MetadataDto();

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set(HttpHeaders.LOCATION,
                    ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(id).toUriString());
            HttpStatus httpStatus;

            if (partitioningService.findById(id).isPresent()) {
                log.info("Update partitioning with the id {}", id);
                partitioningService.findById(id);
                httpStatus = HttpStatus.OK;

            } else {
                log.info("Create partitioning with the id {}", id);
                httpStatus = HttpStatus.CREATED;
            }

            Owner owner = ownerService.insertOrUpdateOwner(convertToEntity(metadataDto.getOwnerDto()));
            Support support = supportService.insertOrUpdateSupport(convertToEntity(metadataDto.getSupportDto()));

            Source source = sourceService.insertOrUpdateSource(convertToEntity(metadataDto.getSourceDto()));

            Survey survey = convertToEntity(metadataDto.getSurveyDto());
            survey.setSource(source);
            survey = surveyService.insertOrUpdateSurvey(survey);

            Campaign campaign = convertToEntity(metadataDto.getCampaignDto());
            campaign.setSurvey(survey);
            campaign = campaignService.insertOrUpdateCampaign(campaign);

            Partitioning partitioning = convertToEntity(metadataDto.getPartitioningDto());
            partitioning.setCampaign(campaign);
            partitioning = partitioningService.insertOrUpdatePartitioning(partitioning);

            Set<Partitioning> partitionings = (campaign.getPartitionings() == null) ? new HashSet<>()
                    : campaign.getPartitionings();
            partitionings.add(partitioning);
            campaign.setPartitionings(partitionings);
            campaign = campaignService.insertOrUpdateCampaign(campaign);

            Set<Campaign> campaigns = (survey.getCampaigns() == null) ? new HashSet<>()
                    : survey.getCampaigns();
            campaigns.add(campaign);
            survey.setCampaigns(campaigns);
            survey = surveyService.insertOrUpdateSurvey(survey);

            Set<Survey> surveys = (source.getSurveys() == null) ? new HashSet<>()
                    : source.getSurveys();
            surveys.add(survey);
            source.setSurveys(surveys);
            source.setOwner(owner);
            source.setSupport(support);

            source = sourceService.insertOrUpdateSource(source);

            Set<Source> sourcesOwner = (owner.getSources() == null) ? new HashSet<>()
                    : owner.getSources();
            sourcesOwner.add(source);
            owner.setSources(sourcesOwner);
            owner = ownerService.insertOrUpdateOwner(owner);

            Set<Source> sourcesSupport = (support.getSources() == null) ? new HashSet<>()
                    : support.getSources();
            sourcesSupport.add(source);
            support.setSources(sourcesSupport);
            support = supportService.insertOrUpdateSupport(support);

            metadataReturn.setOwnerDto(convertToDto(owner));
            metadataReturn.setSupportDto(convertToDto(support));
            metadataReturn.setSourceDto(convertToDto(source));
            metadataReturn.setSurveyDto(convertToDto(survey));
            metadataReturn.setCampaignDto(convertToDto(campaign));
            metadataReturn.setPartitioningDto(convertToDto(partitioning));

            return ResponseEntity.status(httpStatus).headers(responseHeaders).body(metadataReturn);
        } catch (Exception e) {
            log.error("Error in put metadata {}", metadataDto.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error");

        }

    }

    private Support convertToEntity(SupportDto supportDto) {
        return modelmapper.map(supportDto, Support.class);
    }

    private Owner convertToEntity(OwnerDto ownerDto) {
        return modelmapper.map(ownerDto, Owner.class);
    }

    private Source convertToEntity(SourceDto sourceDto) {
        return modelmapper.map(sourceDto, Source.class);
    }

    private Survey convertToEntity(SurveyDto surveyDto) {
        return modelmapper.map(surveyDto, Survey.class);
    }

    private Campaign convertToEntity(CampaignDto campaignDto) {
        return modelmapper.map(campaignDto, Campaign.class);
    }

    private Partitioning convertToEntity(PartitioningDto partitioningDto) {
        return modelmapper.map(partitioningDto, Partitioning.class);
    }

    private SupportDto convertToDto(Support support) {
        return modelmapper.map(support, SupportDto.class);
    }

    private OwnerDto convertToDto(Owner owner) {
        return modelmapper.map(owner, OwnerDto.class);
    }

    private SourceDto convertToDto(Source source) {
        return modelmapper.map(source, SourceDto.class);
    }

    private SurveyDto convertToDto(Survey survey) {
        return modelmapper.map(survey, SurveyDto.class);
    }

    private CampaignDto convertToDto(Campaign campaign) {
        return modelmapper.map(campaign, CampaignDto.class);
    }

    private PartitioningDto convertToDto(Partitioning partitioning) {
        return modelmapper.map(partitioning, PartitioningDto.class);
    }

}
