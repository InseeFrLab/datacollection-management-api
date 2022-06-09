package fr.insee.survey.datacollectionmanagement.questioning.service;

import java.util.List;
import java.util.Set;

import fr.insee.survey.datacollectionmanagement.questioning.domain.QuestioningAccreditation;
import fr.insee.survey.datacollectionmanagement.questioning.domain.SurveyUnit;

public interface QuestioningAccreditationService {

    public List<QuestioningAccreditation> findByIdContact(String id);

    public Set<QuestioningAccreditation> findBySurveyUnit(SurveyUnit su);

}
