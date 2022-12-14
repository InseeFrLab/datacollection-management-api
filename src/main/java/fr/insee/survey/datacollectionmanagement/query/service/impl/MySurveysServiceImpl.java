package fr.insee.survey.datacollectionmanagement.query.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.config.ApplicationConfig;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Partitioning;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Survey;
import fr.insee.survey.datacollectionmanagement.metadata.service.PartitioningService;
import fr.insee.survey.datacollectionmanagement.query.dto.MyQuestioningDto;
import fr.insee.survey.datacollectionmanagement.query.service.MySurveysService;
import fr.insee.survey.datacollectionmanagement.questioning.domain.Questioning;
import fr.insee.survey.datacollectionmanagement.questioning.domain.QuestioningAccreditation;
import fr.insee.survey.datacollectionmanagement.questioning.domain.QuestioningEvent;
import fr.insee.survey.datacollectionmanagement.questioning.service.QuestioningAccreditationService;
import fr.insee.survey.datacollectionmanagement.questioning.service.QuestioningEventService;
import fr.insee.survey.datacollectionmanagement.questioning.util.TypeQuestioningEvent;

@Service
public class MySurveysServiceImpl implements MySurveysService {

    private static final Logger LOGGER = LogManager.getLogger(MySurveysServiceImpl.class);

    @Autowired
    private QuestioningAccreditationService questioningAccreditationService;

    @Autowired
    private PartitioningService partitioningService;

    @Autowired
    private QuestioningEventService questioningEventService;

    @Autowired
    ApplicationConfig applicationConfig;


    @Override
    public List<MyQuestioningDto> getListMySurveys(String id) {
        List<MyQuestioningDto> listSurveys = new ArrayList<>();
        List<QuestioningAccreditation> accreditations = questioningAccreditationService.findByContactIdentifier(id);

        for (QuestioningAccreditation questioningAccreditation : accreditations) {
            MyQuestioningDto surveyDto = new MyQuestioningDto();
            Questioning questioning = questioningAccreditation.getQuestioning();
            Optional<Partitioning> part = partitioningService.findById(questioning.getIdPartitioning());
            if (part.isPresent()) {
                Survey survey = part.get().getCampaign().getSurvey();
                String surveyUnitId = questioning.getSurveyUnit().getIdSu();
                surveyDto.setSurveyWording(survey.getLongWording());
                surveyDto.setSurveyObjectives(survey.getLongObjectives());
                surveyDto.setAccessUrl(
                        applicationConfig.getQuestioningUrl() + "/questionnaire/" + questioning.getModelName()
                                + "/unite-enquetee/" + surveyUnitId);
                surveyDto.setIdentificationCode(surveyUnitId);
                surveyDto.setOpeningDate(new Timestamp(part.get().getOpeningDate().getTime()));
                surveyDto.setClosingDate(new Timestamp(part.get().getClosingDate().getTime()));
                surveyDto.setReturnDate(new Timestamp(part.get().getReturnDate().getTime()));

                Optional<QuestioningEvent> questioningEvent = questioningEventService.getLastQuestioningEvent(
                        questioning, TypeQuestioningEvent.MY_QUESTIONINGS_EVENTS);
                if (questioningEvent.isPresent()) {
                    surveyDto.setQuestioningStatus(questioningEvent.get().getType().name());
                    surveyDto.setQuestioningDate(new Timestamp(questioningEvent.get().getDate().getTime()));
                } else {
                    LOGGER.debug("No questioningEvents found for questioning {} for identifier {}",
                            questioning.getId(), id);
                }

            }
            listSurveys.add(surveyDto);

        }
        LOGGER.info("Get my questionings for id {} - nb results: {}", id, listSurveys.size());
        return listSurveys;
    }

}
