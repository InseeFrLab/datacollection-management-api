package fr.insee.survey.datacollectionmanagement.questioning.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.insee.survey.datacollectionmanagement.questioning.domain.SurveyUnit;

public interface SurveyUnitRepository extends JpaRepository<SurveyUnit, String> {

    public List<SurveyUnit> findAllBySurveyUnitId(String surveyUnitId);

    public List<SurveyUnit> findByCompanyNameIgnoreCase(String companyName);
}
