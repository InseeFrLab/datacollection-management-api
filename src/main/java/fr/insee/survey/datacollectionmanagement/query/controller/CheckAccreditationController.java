package fr.insee.survey.datacollectionmanagement.query.controller;

import fr.insee.survey.datacollectionmanagement.constants.Constants;
import fr.insee.survey.datacollectionmanagement.query.service.CheckAccreditationService;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "4 - Cross domain")
public class CheckAccreditationController {

    static final Logger LOGGER = LoggerFactory.getLogger(CheckAccreditationController.class);

    @Autowired
    private CheckAccreditationService checkAccreditationService;

    @PreAuthorize("@AuthorizeMethodDecider.isInternalUser() "
            + "|| @AuthorizeMethodDecider.isWebClient() "
            + "|| @AuthorizeMethodDecider.isRespondent()")
    @GetMapping(path = Constants.API_CHECK_ACCREDITATIONS)
    public ResponseEntity<?> checkAccreditationV2(
        @RequestParam(required = true) String identifier,
        @RequestParam(required = true) String idSu,
        @RequestParam(required = true) String campaignId) {

        boolean res = checkAccreditationService.checkAccreditation(identifier, idSu, campaignId);
        return new ResponseEntity<>(res, HttpStatus.OK);

    }

}
