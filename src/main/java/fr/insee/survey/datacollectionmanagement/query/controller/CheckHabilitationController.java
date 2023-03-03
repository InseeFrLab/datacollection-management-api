package fr.insee.survey.datacollectionmanagement.query.controller;

import fr.insee.survey.datacollectionmanagement.query.dto.HabilitationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.insee.survey.datacollectionmanagement.constants.Constants;
import fr.insee.survey.datacollectionmanagement.query.service.CheckHabilitationService;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.servlet.http.HttpServletRequest;

@RestController
@Tag(name = "4 - Cross domain")
public class CheckHabilitationController {

    static final Logger LOGGER = LoggerFactory.getLogger(CheckHabilitationController.class);

    @Autowired
    private CheckHabilitationService checkHabilitationService;

    @PreAuthorize("@AuthorizeMethodDecider.isInternalUser() "
            + "|| @AuthorizeMethodDecider.isWebClient() "
            + "|| @AuthorizeMethodDecider.isRespondent()"
            + "|| @AuthorizeMethodDecider.isAdmin()")
    @GetMapping(path = Constants.API_CHECK_HABILITATION,produces = "application/json")
    public ResponseEntity<HabilitationDto> checkHabilitation(
            @RequestParam(required = false) String role,
            @RequestParam(required = true) String id,
            @RequestParam(required = true) String campaign, HttpServletRequest request) {

        return checkHabilitationService.checkHabilitation(role, id,campaign,request);


    }

}
