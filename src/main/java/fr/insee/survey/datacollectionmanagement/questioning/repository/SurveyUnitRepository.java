package fr.insee.survey.datacollectionmanagement.questioning.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.insee.survey.datacollectionmanagement.questioning.domain.SurveyUnit;

public interface SurveyUnitRepository extends JpaRepository<SurveyUnit, String> {
}
