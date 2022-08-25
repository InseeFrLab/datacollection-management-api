package fr.insee.survey.datacollectionmanagement.query.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fr.insee.survey.datacollectionmanagement.query.dto.MySurveyDto;
import fr.insee.survey.datacollectionmanagement.query.service.MySurveysService;

@RestController
@CrossOrigin
public class MySurveysController {


    @Autowired
    private MySurveysService mySurveysService;

    @GetMapping(value = "mySurveys/{id}")
    public ResponseEntity<?>  mySurveysById(@PathVariable("id") String id) {

        List<MySurveyDto> listSurveys = mySurveysService.getListMySurveys(id);
        return new ResponseEntity<>(listSurveys, HttpStatus.OK);
    }

    

}
