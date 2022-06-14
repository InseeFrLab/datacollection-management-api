package fr.insee.survey.datacollectionmanagement.metadata.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.insee.survey.datacollectionmanagement.metadata.domain.Source;

public interface SourceRepository extends JpaRepository<Source, String> {

}
