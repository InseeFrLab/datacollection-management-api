package fr.insee.survey.datacollectionmanagement.metadata.repository;

import fr.insee.survey.datacollectionmanagement.metadata.domain.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyRepository extends JpaRepository<Survey, String> {
}
