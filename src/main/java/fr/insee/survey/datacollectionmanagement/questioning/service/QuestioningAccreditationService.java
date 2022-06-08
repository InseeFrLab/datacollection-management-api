package fr.insee.survey.datacollectionmanagement.questioning.service;

import java.util.List;

import fr.insee.survey.datacollectionmanagement.questioning.domain.QuestioningAccreditation;

public interface QuestioningAccreditationService {

    public List<QuestioningAccreditation> findByIdContact(String id);

}
