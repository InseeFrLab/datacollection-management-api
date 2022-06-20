package fr.insee.survey.datacollectionmanagement.metadata.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.insee.survey.datacollectionmanagement.metadata.domain.Survey;

public interface SurveyRepository extends JpaRepository<Survey, String> {
    
    List<Survey> findByYear(int year);
}
