package fr.insee.survey.datacollectionmanagement.metadata.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import fr.insee.survey.datacollectionmanagement.metadata.domain.Survey;

public interface SurveyRepository extends JpaRepository<Survey, String>, PagingAndSortingRepository<Survey, String>  {
    
    List<Survey> findByYear(int year);
}
