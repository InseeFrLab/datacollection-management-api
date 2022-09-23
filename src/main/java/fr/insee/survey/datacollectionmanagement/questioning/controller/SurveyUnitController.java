package fr.insee.survey.datacollectionmanagement.questioning.controller;

import java.util.NoSuchElementException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.insee.survey.datacollectionmanagement.questioning.domain.SurveyUnit;
import fr.insee.survey.datacollectionmanagement.questioning.service.SurveyUnitService;

@RestController
@CrossOrigin
public class SurveyUnitController {

    static final Logger LOGGER = LoggerFactory.getLogger(SurveyUnitController.class);

    @Autowired
    private SurveyUnitService surveyUnitService;

    @GetMapping(value = "surveyUnits")
    public Page<SurveyUnit> findSurveyUnits(
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "20") Integer size,
        @RequestParam(defaultValue = "idSu") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return surveyUnitService.findAll(pageable);
    }

    @GetMapping(value = "surveyUnits/{id}")
    public ResponseEntity<?> findSurveyUnit(@PathVariable("id") String id) {
        SurveyUnit surveyUnit = null;
        try {
            surveyUnit = surveyUnitService.findbyId(StringUtils.upperCase(id));
            return new ResponseEntity<>(surveyUnit, HttpStatus.OK);
        }
        catch (NoSuchElementException e) {
            return new ResponseEntity<>(surveyUnit, HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return new ResponseEntity<String>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping(value = "surveyUnits/{id}")
    public ResponseEntity<?> putSurveyUnit(@PathVariable("id") String id, @RequestBody SurveyUnit surveyUnit) {
        if (StringUtils.isBlank(surveyUnit.getIdSu()) || !surveyUnit.getIdSu().equalsIgnoreCase(id)) {
            return new ResponseEntity<>("id and surveyUnit identifier don't match", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(surveyUnitService.updateSurveyUnit(surveyUnit), HttpStatus.OK);
    }

    @DeleteMapping(value = "surveyUnits/{id}")
    public ResponseEntity<?> deleteSurveyUnit(@PathVariable("id") String id) {
        try {
            surveyUnitService.deleteSurveyUnit(id);
            return new ResponseEntity<>("SurveyUnit deleted", HttpStatus.OK);
        }
        catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>("SurveyUnit not found", HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return new ResponseEntity<String>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
