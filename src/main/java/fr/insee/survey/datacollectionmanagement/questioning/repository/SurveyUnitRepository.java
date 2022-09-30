package fr.insee.survey.datacollectionmanagement.questioning.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fr.insee.survey.datacollectionmanagement.questioning.domain.SurveyUnit;

public interface SurveyUnitRepository extends JpaRepository<SurveyUnit, String> {

    public List<SurveyUnit> findAllByIdentificationCode(String identificationCode);

    public List<SurveyUnit> findByIdentificationNameIgnoreCase(String identificationName);

    @Query(nativeQuery = true, value = "SELECT *  FROM survey_unit ORDER BY random() LIMIT 1")
    public SurveyUnit findRandomSurveyUnit();
}
