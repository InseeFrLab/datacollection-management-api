package fr.insee.survey.datacollectionmanagement.query.controller;

import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import fr.insee.survey.datacollectionmanagement.constants.Constants;
import fr.insee.survey.datacollectionmanagement.metadata.service.PartitioningService;
import fr.insee.survey.datacollectionmanagement.questioning.domain.Questioning;
import fr.insee.survey.datacollectionmanagement.questioning.domain.SurveyUnit;
import fr.insee.survey.datacollectionmanagement.questioning.dto.QuestioningDto;
import fr.insee.survey.datacollectionmanagement.questioning.service.QuestioningService;
import fr.insee.survey.datacollectionmanagement.questioning.service.SurveyUnitService;
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
        + "|| @AuthorizeMethodDecider.isWebClient() ")
@Slf4j
@Tag(name = "2 - Questioning", description = "Enpoints to create, update, delete and find entities around the questionings")
public class QuestioningController {

    @Autowired
    private QuestioningService questioningService;

    @Autowired
    private SurveyUnitService surveyUnitService;

    @Autowired
    private PartitioningService partitioningService;

    @Autowired
    private ModelMapper modelMapper;

    @Operation(summary = "Search for a questioning by id")
    @GetMapping(value = Constants.API_QUESTIONINGS_ID, produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = QuestioningDto.class))),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    public ResponseEntity<?> getQuestioning(@PathVariable("id") Long id) {

        Optional<Questioning> optQuestioning = null;
        try {
            optQuestioning = questioningService.findbyId(id);
            if (optQuestioning.isPresent())
                return new ResponseEntity<>(convertToDto(optQuestioning.get()), HttpStatus.OK);
            else {
                log.warn("Questioning {} does not exist", id);
                return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<String>("Error", HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Create or update questioning")
    @PostMapping(value = Constants.API_QUESTIONINGS, produces = "application/json", consumes = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(schema = @Schema(implementation = QuestioningDto.class))),
            @ApiResponse(responseCode = "404", description = "NotFound")
    })
    public ResponseEntity<?> postQuestioning(@RequestBody QuestioningDto questioningDto) {
        Optional<SurveyUnit> optSu = surveyUnitService.findbyId(questioningDto.getSurveyUnitId());

        if (!optSu.isPresent()) {
            log.warn("survey unit {} does not exist", questioningDto.getSurveyUnitId());
            return new ResponseEntity<>("survey unit does not exist", HttpStatus.NOT_FOUND);
        }

        SurveyUnit su = optSu.get();

        if (!partitioningService.findById(questioningDto.getIdPartitioning()).isPresent()) {
            log.warn("partitioning {} does not exist", questioningDto.getIdPartitioning());
            return new ResponseEntity<>("partitioning does not exist", HttpStatus.NOT_FOUND);
        }
        Questioning questioning = convertToEntity(questioningDto);
        questioning.setSurveyUnit(su);
        questioning = questioningService.saveQuestioning(questioning);
        su.getQuestionings().add(questioning);
        surveyUnitService.saveSurveyUnit(su);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(HttpHeaders.LOCATION, ServletUriComponentsBuilder.fromCurrentRequest().toUriString());
        return ResponseEntity.status(HttpStatus.CREATED).headers(responseHeaders).body(convertToDto(questioning));

    }

    @Operation(summary = "Search for questionings by survey unit id")
    @GetMapping(value = Constants.API_SURVEY_UNITS_ID_QUESTIONINGS, produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = QuestioningDto.class)))),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    public ResponseEntity<?> getQuestioningsBySurveyUnit(@PathVariable("id") String id) {
        try {
            Optional<SurveyUnit> optSu = surveyUnitService.findbyId(StringUtils.upperCase(id));
            if (optSu.isPresent())
                return new ResponseEntity<>(
                        optSu.get().getQuestionings().stream().map(q -> convertToDto(q)).collect(Collectors.toList()),
                        HttpStatus.OK);
            else {
                log.warn("survey unit {} does not exist", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("survey unit does not exist");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error");
        }

    }

    private Questioning convertToEntity(QuestioningDto questioningDto) {
        return modelMapper.map(questioningDto, Questioning.class);
    }

    private QuestioningDto convertToDto(Questioning questioning) {
        return modelMapper.map(questioning, QuestioningDto.class);
    }

}
