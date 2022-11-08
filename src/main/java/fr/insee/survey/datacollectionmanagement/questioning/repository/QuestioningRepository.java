package fr.insee.survey.datacollectionmanagement.questioning.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.insee.survey.datacollectionmanagement.questioning.domain.Questioning;

public interface QuestioningRepository extends JpaRepository<Questioning, Long> {

    public Set<Questioning> findByIdPartitioning(String idPartitioning);

    public Questioning findByModelNameAndIdPartitioningAndSurveyUnitIdSu(String modelName, String idPartitioning,
            String surveyUnitIdSu);

}
