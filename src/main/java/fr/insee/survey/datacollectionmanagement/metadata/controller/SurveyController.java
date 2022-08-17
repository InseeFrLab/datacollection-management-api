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

import fr.insee.survey.datacollectionmanagement.metadata.domain.Survey;
import fr.insee.survey.datacollectionmanagement.metadata.service.SurveyService;

@RestController
@CrossOrigin
public class SurveyController {

    static final Logger LOGGER = LoggerFactory.getLogger(SurveyController.class);

    @Autowired
    private SurveyService surveyService;

    @GetMapping(value = "surveys")
    public Page<Survey> findSurveys(
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "20") Integer size,
        @RequestParam(defaultValue = "id") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return surveyService.findAll(pageable);
    }

    @GetMapping(value = "surveys/{id}")
    public ResponseEntity<?> findSurvey(@PathVariable("id") String id) {
        Survey survey = null;
        try {
            survey = surveyService.findbyId(StringUtils.upperCase(id));
            return new ResponseEntity<>(survey, HttpStatus.OK);
        }
        catch (NoSuchElementException e) {
            return new ResponseEntity<>(survey, HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return new ResponseEntity<String>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping(value = "surveys/{id}")
    public ResponseEntity<?> putSurvey(@PathVariable("id") String id, @RequestBody Survey survey) {
        if (StringUtils.isBlank(survey.getId()) || !survey.getId().equalsIgnoreCase(id)) {
            return new ResponseEntity<>("id and survey identifier don't match", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(surveyService.updateSurvey(survey), HttpStatus.OK);
    }


}
