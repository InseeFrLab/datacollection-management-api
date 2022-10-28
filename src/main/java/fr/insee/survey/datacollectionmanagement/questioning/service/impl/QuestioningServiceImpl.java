package fr.insee.survey.datacollectionmanagement.questioning.service.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.metadata.domain.Partitioning;
import fr.insee.survey.datacollectionmanagement.questioning.domain.Questioning;
import fr.insee.survey.datacollectionmanagement.questioning.domain.SurveyUnit;
import fr.insee.survey.datacollectionmanagement.questioning.repository.QuestioningRepository;
import fr.insee.survey.datacollectionmanagement.questioning.service.QuestioningAccreditationService;
import fr.insee.survey.datacollectionmanagement.questioning.service.QuestioningEventService;
import fr.insee.survey.datacollectionmanagement.questioning.service.QuestioningService;
import fr.insee.survey.datacollectionmanagement.questioning.service.SurveyUnitService;

@Service
public class QuestioningServiceImpl implements QuestioningService {

    @Autowired
    private QuestioningRepository questioningRepository;

    @Autowired
    private SurveyUnitService surveyUnitService;

    @Autowired
    private QuestioningEventService questioningEventService;

    @Autowired
    private QuestioningAccreditationService questioningAccreditationService;

    @Override
    public Page<Questioning> findAll(Pageable pageable) {
        return questioningRepository.findAll(pageable);
    }

    @Override
    public Questioning findbyId(Long id) {
        return questioningRepository.findById(id).orElseThrow();
    }

    @Override
    public Questioning saveQuestioning(Questioning questioning) {
        return questioningRepository.save(questioning);
    }

    @Override
    public void deleteQuestioning(Long id) {
        questioningRepository.deleteById(id);
    }

    @Override
    public Set<Questioning> findByIdPartitioning(String idPartitioning) {
        return questioningRepository.findByIdPartitioning(idPartitioning);
    }

    @Override
    public int deleteQuestioningsOfOnePartitioning(Partitioning partitioning) {
        int nbQuestioningDeleted = 0;
        Set<Questioning> setQuestionings = findByIdPartitioning(partitioning.getId());
        for (Questioning q : setQuestionings) {
            SurveyUnit su = q.getSurveyUnit();
            su.getQuestionings().remove(q);
            surveyUnitService.saveSurveyUnit(su);
            q.getQuestioningEvents().stream().forEach(qe -> questioningEventService.deleteQuestioningEvent(qe.getId()));
            q.getQuestioningAccreditations().stream()
                    .forEach(qa -> questioningAccreditationService.deleteAccreditation(qa));
            deleteQuestioning(q.getId());
            nbQuestioningDeleted++;
        }
        return nbQuestioningDeleted;
    }

}
