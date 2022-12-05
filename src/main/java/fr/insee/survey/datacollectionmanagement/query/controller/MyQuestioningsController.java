package fr.insee.survey.datacollectionmanagement.query.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping(value = Constants.API_MY_QUESTIONINGS_ID)
    @PreAuthorize("@AuthorizeMethodDecider.isInternalUser() "
            + "|| @AuthorizeMethodDecider.isWebClient() "
            + "|| @AuthorizeMethodDecider.isRespondent()")
    public List<MyQuestioningDto> findById(@PathVariable("id") String id) {

        List<MyQuestioningDto> listSurveys = mySurveysService.getListMySurveys(id);

        return listSurveys;

    }
}
