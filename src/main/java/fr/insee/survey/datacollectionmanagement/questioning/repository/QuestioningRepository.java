package fr.insee.survey.datacollectionmanagement.questioning.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.insee.survey.datacollectionmanagement.questioning.domain.Questioning;

public interface QuestioningRepository extends JpaRepository<Questioning, Long> {

    public List<Questioning> findByIdPartitioning(String idPartitioning);

}
