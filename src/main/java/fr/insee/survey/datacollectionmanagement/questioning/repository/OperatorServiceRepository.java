package fr.insee.survey.datacollectionmanagement.questioning.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.insee.survey.datacollectionmanagement.questioning.domain.OperatorService;

public interface OperatorServiceRepository extends JpaRepository<OperatorService, Long> {
}
