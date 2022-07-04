package fr.insee.survey.datacollectionmanagement.questioning.service;

import java.util.List;
import java.util.Set;

import fr.insee.survey.datacollectionmanagement.questioning.domain.QuestioningAccreditation;
import fr.insee.survey.datacollectionmanagement.questioning.domain.SurveyUnit;

public interface QuestioningAccreditationService {

    public List<QuestioningAccreditation> findByIdContact(String id);

    public Set<QuestioningAccreditation> findBySurveyUnit(SurveyUnit su);

    public List<String> findIdContactsByPartitionigAccredications(String idPartitioning);

    public List<String> findIdPartitioningsByContactAccreditations(String idContact);

    public List<String> findIdContactsByIdSource(String idSource);

    public List<String> findIdContactsByYear(Integer year);

    public List<String> findIdContactsByPeriod(String period);

    public List<String> findIdContactsBySourceYearPeriod(String idSource, Integer year, String period);

}
