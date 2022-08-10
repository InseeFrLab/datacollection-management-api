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

import fr.insee.survey.datacollectionmanagement.query.service.CheckAccreditationService;

@RestController
@CrossOrigin
public class CheckAccreditationController {

    static final Logger LOGGER = LoggerFactory.getLogger(CheckAccreditationController.class);

    @Autowired
    private CheckAccreditationService checkAccreditationService;

    @GetMapping(path = "checkAccreditationV2")
    public ResponseEntity<?> checkAccreditationV2(
        @RequestParam(required = true) String identifier,
        @RequestParam(required = true) String idSu,
        @RequestParam(required = true) String campaignId) {

        boolean res = checkAccreditationService.checkAccreditationV2(identifier, idSu, campaignId);
//        LOGGER.info("Check accreditation V2: identifier = {},  idSu= {}, campaignId= {} - res = {}", identifier, idSu, campaignId, res);

        return new ResponseEntity<>(res, HttpStatus.OK);

    }

    @GetMapping(path = "checkAccreditationV3")
    public ResponseEntity<?> checkAccreditationV3(
        @RequestParam(required = true) String identifier,
        @RequestParam(required = true) String idSu,
        @RequestParam(required = true) String campaignId) {

        boolean res = checkAccreditationService.checkAccreditationV3(identifier, idSu, campaignId);
//        LOGGER.info("Check accreditation V3: identifier = {},  idSu= {}, campaignId= {} - res = {}", identifier, idSu, campaignId, res);

        return new ResponseEntity<>(res, HttpStatus.OK);

    }

}
