package fr.insee.survey.datacollectionmanagement.query.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.insee.survey.datacollectionmanagement.constants.Constants;
import fr.insee.survey.datacollectionmanagement.query.service.CheckAccreditationService;

@RestController
@CrossOrigin
public class CheckHabilitationController {

    static final Logger LOGGER = LoggerFactory.getLogger(CheckHabilitationController.class);

    @Autowired
    private CheckAccreditationService checkAccreditationService;

    @GetMapping(path = Constants.API_CHECK_HABILITATION)
    public ResponseEntity<?> checkHabilitation(
        @RequestParam(required = true) String identifier,
        @RequestParam(required = true) String idSu,
        @RequestParam(required = true) String campaignId) {

        boolean res = checkAccreditationService.checkAccreditation(identifier, idSu, campaignId);
        return new ResponseEntity<>(res, HttpStatus.OK);

    }

}
