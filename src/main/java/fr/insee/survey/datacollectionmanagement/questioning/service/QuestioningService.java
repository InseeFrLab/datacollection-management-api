package fr.insee.survey.datacollectionmanagement.questioning.service;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.insee.survey.datacollectionmanagement.metadata.domain.Partitioning;
import fr.insee.survey.datacollectionmanagement.questioning.domain.Questioning;

public interface QuestioningService {

    public Page<Questioning> findAll(Pageable pageable);

    public Questioning findbyId(Long id);

    public Questioning saveQuestioning(Questioning questioning);

    public void deleteQuestioning(Long id);

    public Set<Questioning> findByIdPartitioning(String idPartitioning);

    public Questioning findByIdPartitioningAndSurveyUnitIdSu(String idPartitioning, String surveyUnitIdSu);

    /**
     * Delete questionings attached to one partitioning
     * 
     * @param partitioning
     * @return nb questioning deleted
     */
    public int deleteQuestioningsOfOnePartitioning(Partitioning partitioning);

}
