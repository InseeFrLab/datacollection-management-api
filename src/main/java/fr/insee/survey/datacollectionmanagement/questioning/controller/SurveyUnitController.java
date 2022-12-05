package fr.insee.survey.datacollectionmanagement.questioning.controller;

import java.text.ParseException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import fr.insee.survey.datacollectionmanagement.constants.Constants;
import fr.insee.survey.datacollectionmanagement.questioning.domain.SurveyUnit;
import fr.insee.survey.datacollectionmanagement.questioning.dto.SurveyUnitDto;
import fr.insee.survey.datacollectionmanagement.questioning.service.SurveyUnitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@PreAuthorize("@AuthorizeMethodDecider.isInternalUser() "
        + "|| @AuthorizeMethodDecider.isWebClient() ")
@Tag(name = "2 - Questioning", description = "Enpoints to create, update, delete and find entities around the questionings")
@Slf4j
public class SurveyUnitController {

    static final Logger LOGGER = LoggerFactory.getLogger(SurveyUnitController.class);

    @Autowired
    private SurveyUnitService surveyUnitService;

    @Autowired
    private ModelMapper modelMapper;

    @Operation(summary = "Search for a survey units, paginated")
    @GetMapping(value = Constants.API_SURVEY_UNITS, produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = SurveyUnitDto.class))),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    public Page<SurveyUnitDto> getSurveyUnits(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(defaultValue = "idSu") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<SurveyUnit> pageC = surveyUnitService.findAll(pageable);
        List<SurveyUnitDto> listSuDto = pageC.stream().map(c -> convertToDto(c)).collect(Collectors.toList());
        return new SurveyUnitPage(listSuDto, pageable, pageC.getTotalElements());
    }

    @Operation(summary = "Search for a survey unit by its id")
    @GetMapping(value = Constants.API_SURVEY_UNITS_ID, produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = SurveyUnitDto.class))),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    public ResponseEntity<?> findSurveyUnit(@PathVariable("id") String id) {
        SurveyUnit surveyUnit = null;
        try {
            surveyUnit = surveyUnitService.findbyId(StringUtils.upperCase(id));
            return ResponseEntity.status(HttpStatus.OK).body(convertToDto(surveyUnit));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("survey unit not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error");
        }

    }

    @Operation(summary = "Create or update a survey unit")
    @PutMapping(value = Constants.API_SURVEY_UNITS_ID, produces = "application/json", consumes = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = SurveyUnitDto.class))),
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(schema = @Schema(implementation = SurveyUnitDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> putSurveyUnit(@PathVariable("id") String id, @RequestBody SurveyUnitDto surveyUnitDto) {
        if (StringUtils.isBlank(surveyUnitDto.getIdSu()) || !surveyUnitDto.getIdSu().equalsIgnoreCase(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("id and idSu don't match");

        }

        SurveyUnit surveyUnit;
        HttpStatus responseStatus;
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(HttpHeaders.LOCATION,
                ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(surveyUnitDto.getIdSu()).toUriString());

        try {
            surveyUnit = convertToEntity(surveyUnitDto);
        } catch (ParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Impossible to parse survey unit");
        }

        try {
            surveyUnitService.findbyId(surveyUnitDto.getIdSu());
            responseStatus = HttpStatus.OK;
        } catch (NoSuchElementException e) {
            LOGGER.info("Creating survey with the id {}", surveyUnitDto.getIdSu());
            responseStatus = HttpStatus.CREATED;
        }
        return ResponseEntity.status(responseStatus)
                .body(convertToDto(surveyUnitService.saveSurveyUnitAndAddress(surveyUnit)));

    }

    @Operation(summary = "Delete a survey unit by its id")
    @DeleteMapping(value = Constants.API_SURVEY_UNITS_ID, produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> deleteSurveyUnit(@PathVariable("id") String id) {
        SurveyUnit surveyUnit;
        try {
            surveyUnit = surveyUnitService.findbyId(StringUtils.upperCase(id));
            if (!surveyUnit.getQuestionings().isEmpty()) {
                log.warn("Some questionings exist for the survey unit {}, the survey unit can't be deleted", id);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Some questionings exist for this survey unit, the survey unit can't be deleted");
            }
            surveyUnitService.deleteSurveyUnit(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Survey unit deleted");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("survey unit not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error");
        }
    }

    private SurveyUnitDto convertToDto(SurveyUnit surveyUnit) {
        return modelMapper.map(surveyUnit, SurveyUnitDto.class);
    }

    private SurveyUnit convertToEntity(SurveyUnitDto surveyUnitDto) throws ParseException {
        return modelMapper.map(surveyUnitDto, SurveyUnit.class);
    }

    class SurveyUnitPage extends PageImpl<SurveyUnitDto> {

        private static final long serialVersionUID = 656181199902518234L;

        public SurveyUnitPage(List<SurveyUnitDto> content, Pageable pageable, long total) {
            super(content, pageable, total);
        }
    }
}
