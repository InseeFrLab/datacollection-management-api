package fr.insee.survey.datacollectionmanagement.metadata.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import fr.insee.survey.datacollectionmanagement.constants.Constants;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Campaign;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Partitioning;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Source;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Survey;
import fr.insee.survey.datacollectionmanagement.metadata.dto.SurveyDto;
import fr.insee.survey.datacollectionmanagement.metadata.service.SourceService;
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
import lombok.extern.log4j.Log4j2;

@RestController
@CrossOrigin
@Log4j2
@Tag(name = "3 - Metadata", description = "Enpoints to create, update, delete and find entities in metadata domain")
public class SurveyController {

    @Autowired
    private SurveyService surveyService;

    @Autowired
    private SourceService sourceService;

    @Autowired
    private ViewService viewService;

    @Autowired
    private ModelMapper modelmapper;

    private QuestioningService questioningService;

    @Operation(summary = "Search for surveys by the source id")
    @GetMapping(value = Constants.API_SOURCES_ID_SURVEYS, produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = SurveyDto.class)))),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> getSurveysBySource(@PathVariable("id") String id) {

        try {
            Optional<Source> source = sourceService.findById(id);
            if (!source.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("source does not exist");
            }
            source = sourceService.findById(id);
            return ResponseEntity.ok()
                    .body(source.get().getSurveys().stream().map(s -> convertToDto(s)).collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error");
        }

    }

    @Operation(summary = "Search for a survey by its id")
    @GetMapping(value = Constants.API_SURVEYS_ID, produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = SurveyDto.class))),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> getSurvey(@PathVariable("id") String id) {
        try {
            Optional<Survey> survey = surveyService.findById(StringUtils.upperCase(id));
            if (!survey.isPresent()) {
                log.warn("Survey {} does not exist", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("survey does not exist");
            }
            return ResponseEntity.ok().body(convertToDto(survey.get()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error");
        }

    }

    @Operation(summary = "Update or create a survey")
    @PutMapping(value = Constants.API_SURVEYS_ID, produces = "application/json", consumes = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = SurveyDto.class))),
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(schema = @Schema(implementation = SurveyDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> putSurvey(@PathVariable("id") String id, @RequestBody SurveyDto surveyDto) {
        if (StringUtils.isBlank(surveyDto.getId()) || !surveyDto.getId().equalsIgnoreCase(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("id and idSurvey don't match");
        }
        Survey survey;
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(HttpHeaders.LOCATION,
                ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(surveyDto.getId()).toUriString());
        HttpStatus httpStatus;

        try {
            surveyService.findById(id);
            log.info("Update survey with the id {}", surveyDto.getId());
            httpStatus = HttpStatus.OK;

        } catch (NoSuchElementException e) {
            log.info("Creating survey with the id {}", surveyDto.getId());
            httpStatus = HttpStatus.CREATED;
        }

        survey = surveyService.insertOrUpdateSurvey(convertToEntity(surveyDto));
        Source source = survey.getSource();
        source.getSurveys().add(survey);
        sourceService.insertOrUpdateSource(source);
        return ResponseEntity.status(httpStatus).headers(responseHeaders).body(convertToDto(survey));
    }

    @Operation(summary = "Delete a survey, its campaigns, partitionings, questionings ...")
    @DeleteMapping(value = Constants.API_SURVEYS_ID)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @Transactional
    public ResponseEntity<?> deleteSurvey(@PathVariable("id") String id) {

        try {
            Optional<Survey> survey = surveyService.findById(id);
            if (!survey.isPresent()) {
                log.warn("Survey {} does not exist", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Survey does not exist");
            }

            int nbQuestioningDeleted = 0;
            int nbViewDeleted = 0;

            Source source = survey.get().getSource();
            source.getSurveys().remove(survey.get());
            sourceService.insertOrUpdateSource(source);
            surveyService.deleteSurveyById(id);
            List<Partitioning> listPartitionings = new ArrayList<>();

            survey.get().getCampaigns().stream().forEach(c -> listPartitionings.addAll(c.getPartitionings()));

            for (Campaign campaign : survey.get().getCampaigns()) {
                viewService.findViewByCampaignId(campaign.getId()).stream()
                        .forEach(v -> viewService.deleteView(v));
            }
            for (Partitioning partitioning : listPartitionings) {
                questioningService.findByIdPartitioning(partitioning.getId()).stream()
                        .forEach(q -> questioningService.deleteQuestioning(q.getId()));
            }

            for (Campaign campaign : survey.get().getCampaigns()) {
                nbViewDeleted += viewService.deleteViewsOfOneCampaign(campaign);
            }
            for (Partitioning partitioning : listPartitionings) {
                nbQuestioningDeleted += questioningService.deleteQuestioningsOfOnePartitioning(partitioning);
            }
            log.info("Source {} deleted and all its metadata children - {} questioning deleted - {} view deleted", id,
                    nbQuestioningDeleted, nbViewDeleted);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Survey deleted");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error");
        }
    }

    private SurveyDto convertToDto(Survey survey) {
        return modelmapper.map(survey, SurveyDto.class);
    }

    private Survey convertToEntity(SurveyDto surveyDto) {
        return modelmapper.map(surveyDto, Survey.class);
    }

    class SurveyPage extends PageImpl<SurveyDto> {

        public SurveyPage(List<SurveyDto> content, Pageable pageable, long total) {
            super(content, pageable, total);
        }
    }

}
