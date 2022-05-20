package fr.insee.survey.datacollectionmanagement.questioning.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.insee.survey.datacollectionmanagement.questioning.domain.Operator;

public interface OperatorRepository extends JpaRepository<Operator, Long> {
}
