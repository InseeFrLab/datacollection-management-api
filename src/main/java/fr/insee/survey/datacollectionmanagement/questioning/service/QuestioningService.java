package fr.insee.survey.datacollectionmanagement.questioning.service;

import java.util.List;

import fr.insee.survey.datacollectionmanagement.questioning.domain.Questioning;

public interface QuestioningService {

    public List<Questioning> fingByIdPartitioning(String idPartitioning);

    List<Questioning> fingByIdPartitioning(String idPartitioning, int limit);

}
