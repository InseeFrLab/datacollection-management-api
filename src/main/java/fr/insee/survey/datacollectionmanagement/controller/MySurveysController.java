package fr.insee.survey.datacollectionmanagement.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import fr.insee.survey.datacollectionmanagement.controller.dto.MySurveyDto;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Partitioning;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Survey;
import fr.insee.survey.datacollectionmanagement.metadata.repository.PartitioningRepository;
import fr.insee.survey.datacollectionmanagement.questioning.domain.Questioning;
import fr.insee.survey.datacollectionmanagement.questioning.domain.QuestioningAccreditation;
import fr.insee.survey.datacollectionmanagement.questioning.repository.QuestioningAccreditationRepository;

@RestController
@CrossOrigin
public class MySurveysController {

    private static final String STROMAE_URL = "https://dev.insee.io/questionnaire/";

    @Autowired
    private QuestioningAccreditationRepository questioningAccreditationRepository;

    @Autowired
    private PartitioningRepository partitioningRepository;

    @GetMapping(value = "mySurveys/{id}")
    public List<MySurveyDto> findById(@PathVariable("id") String id) {
        
        List<MySurveyDto> listSurveys = new ArrayList<>();
        List<QuestioningAccreditation> accreditations = questioningAccreditationRepository.findByIdContact(id);

        for (QuestioningAccreditation questioningAccreditation : accreditations) {
            MySurveyDto surveyDto = new MySurveyDto();
            Questioning questioning = questioningAccreditation.getQuestioning();
            Partitioning part = partitioningRepository.findById(questioning.getIdPartitioning()).orElse(null);
            if (part != null) {
                Survey survey = part.getCampaign().getSurvey();
                String surveyUnitId = questioning.getSurveyUnit().getSurveyUnitId();
                surveyDto.setSurveyWording(survey.getLongWording());
                surveyDto.setSurveyObjectives(survey.getLongObjectives());
                surveyDto.setMonitoringDate(new Timestamp(part.getReturnDate().getTime()));
                surveyDto.setAccessUrl(STROMAE_URL + part.getCampaign().getCampaignId() + "/unite-enquetee/" + surveyUnitId);
                surveyDto.setSurveyUnitId(surveyUnitId);
                surveyDto.setMonitoringStatus(part.getStatus());
            }

            listSurveys.add(surveyDto);

        }
        return listSurveys;
    }

}
