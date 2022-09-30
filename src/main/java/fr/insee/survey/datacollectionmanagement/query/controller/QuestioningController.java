package fr.insee.survey.datacollectionmanagement.query.controller;

import java.util.NoSuchElementException;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@CrossOrigin
public class QuestioningController {

    static final Logger LOGGER = LoggerFactory.getLogger(QuestioningController.class);

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
        @ApiResponse(responseCode = "404", description = "Not found"), @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    public ResponseEntity<?> getQuestioning(@PathVariable("id") Long id) {

        Questioning questioning = null;
        try {
            questioning = questioningService.findbyId(id);
            return new ResponseEntity<>(convertToDto(questioning), HttpStatus.OK);
        }
        catch (NoSuchElementException e) {
            return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
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
        SurveyUnit su;
        try {
            su = surveyUnitService.findbyId(questioningDto.getSurveyUnitId());
        }
        catch (NoSuchElementException e) {
            return new ResponseEntity<>("survey unit does not exist", HttpStatus.NOT_FOUND);
        }
        try {
            partitioningService.findById(questioningDto.getIdPartitioning());
        }
        catch (NoSuchElementException e) {
            return new ResponseEntity<>("partitioning does not exist", HttpStatus.NOT_FOUND);
        }
        Questioning questioning = convertToEntity(questioningDto);
        questioning.setSurveyUnit(su);
        questioning = questioningService.saveQuestioning(questioning);
        su.getQuestionings().add(questioning);
        surveyUnitService.saveSurveyUnit(su);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(HttpHeaders.LOCATION, ServletUriComponentsBuilder.fromCurrentRequest().path(questioning.getId().toString()).toUriString());
        return ResponseEntity.status(HttpStatus.CREATED).headers(responseHeaders).body(convertToDto(questioning));

    }

    private Questioning convertToEntity(QuestioningDto questioningDto) {
        return modelMapper.map(questioningDto, Questioning.class);
    }

    private QuestioningDto convertToDto(Questioning questioning) {
        return modelMapper.map(questioning, QuestioningDto.class);
    }

}
