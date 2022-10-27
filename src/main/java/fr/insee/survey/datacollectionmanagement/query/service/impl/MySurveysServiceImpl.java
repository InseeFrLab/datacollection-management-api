package fr.insee.survey.datacollectionmanagement.query.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.metadata.domain.Partitioning;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Survey;
import fr.insee.survey.datacollectionmanagement.metadata.service.PartitioningService;
import fr.insee.survey.datacollectionmanagement.metadata.util.PartitioningStatusEnum;
import fr.insee.survey.datacollectionmanagement.query.dto.MySurveyDto;
import fr.insee.survey.datacollectionmanagement.query.service.MySurveysService;
import fr.insee.survey.datacollectionmanagement.questioning.domain.Questioning;
import fr.insee.survey.datacollectionmanagement.questioning.domain.QuestioningAccreditation;
import fr.insee.survey.datacollectionmanagement.questioning.domain.QuestioningEvent;
import fr.insee.survey.datacollectionmanagement.questioning.service.QuestioningAccreditationService;
import fr.insee.survey.datacollectionmanagement.questioning.service.QuestioningEventService;

@Service
public class MySurveysServiceImpl implements MySurveysService {

    private static final Logger LOGGER = LogManager.getLogger(MySurveysServiceImpl.class);

    private static final String STROMAE_URL = "https://dev.insee.io/questionnaire/";

    @Autowired
    private QuestioningAccreditationService questioningAccreditationService;

    @Autowired
    private PartitioningService partitioningService;

    @Autowired
    private QuestioningEventService questioningEventService;

    @Override
    public List<MySurveyDto> getListMySurveys(String id) {
        List<MySurveyDto> listSurveys = new ArrayList<>();
        List<QuestioningAccreditation> accreditations = questioningAccreditationService.findByContactIdentifier(id);

        for (QuestioningAccreditation questioningAccreditation : accreditations) {
            MySurveyDto surveyDto = new MySurveyDto();
            Questioning questioning = questioningAccreditation.getQuestioning();
            Optional<Partitioning> part = partitioningService.findById(questioning.getIdPartitioning());
            if (part.isPresent()) {
                Survey survey = part.get().getCampaign().getSurvey();
                String surveyUnitId = questioning.getSurveyUnit().getIdSu();
                surveyDto.setSurveyWording(survey.getLongWording());
                surveyDto.setSurveyObjectives(survey.getLongObjectives());
                surveyDto.setAccessUrl(
                        STROMAE_URL + part.get().getCampaign().getId() + "/unite-enquetee/" + surveyUnitId);
                surveyDto.setIdentificationCode(surveyUnitId);
                PartitioningStatusEnum partitioningStatusEnum = partitioningService
                        .calculatePartitioningStatus(part.get());
                Date monitoringDate = partitioningService.calculatePartitioningDate(part.get(), partitioningStatusEnum);
                surveyDto.setMonitoringStatus(partitioningStatusEnum.getValue());
                surveyDto.setMonitoringDate(monitoringDate == null ? null : new Timestamp(monitoringDate.getTime()));

                QuestioningEvent questioningEvent;
                try {
                    questioningEvent = questioningEventService.getLastQuestioningEvent(questioning);
                    surveyDto.setQuestioningStatus(questioningEvent.getType().name());
                    surveyDto.setQuestioningDate(new Timestamp(questioningEvent.getDate().getTime()));
                } catch (NoSuchElementException e) {
                    LOGGER.info("No questiongEvents PARTIELINT or VALINT found for questioning {} for identifier {}",
                            questioning.getId(), id);
                }

            }
            listSurveys.add(surveyDto);

        }
        return listSurveys;
    }

}
