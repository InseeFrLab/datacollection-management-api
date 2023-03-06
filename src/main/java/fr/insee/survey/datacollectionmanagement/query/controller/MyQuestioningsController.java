package fr.insee.survey.datacollectionmanagement.query.controller;

import java.util.List;

import fr.insee.survey.datacollectionmanagement.config.ApplicationConfig;
import fr.insee.survey.datacollectionmanagement.config.auth.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fr.insee.survey.datacollectionmanagement.constants.Constants;
import fr.insee.survey.datacollectionmanagement.query.dto.MyQuestioningDto;
import fr.insee.survey.datacollectionmanagement.query.service.MySurveysService;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "4 - Cross domain")
public class MyQuestioningsController {

    @Autowired
    private MySurveysService mySurveysService;

    @Autowired
    ApplicationConfig config;

    @GetMapping(value = Constants.API_MY_QUESTIONINGS_ID)
    @PreAuthorize("@AuthorizeMethodDecider.isInternalUser() "
            + "|| @AuthorizeMethodDecider.isWebClient() "
            + "|| @AuthorizeMethodDecider.isRespondent()"
            + "|| @AuthorizeMethodDecider.isAdmin() ")
    public List<MyQuestioningDto> findById() {

        String idec=null;

        if (config.getAuthType().equals("OIDC")) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            final Jwt jwt = (Jwt) authentication.getPrincipal();
            idec=jwt.getClaimAsString(config.getIdClaim()).toUpperCase();
        }
        else{
           idec="anonymous";
        }

        List<MyQuestioningDto> listSurveys = mySurveysService.getListMySurveys(idec);

        return listSurveys;

    }
}
