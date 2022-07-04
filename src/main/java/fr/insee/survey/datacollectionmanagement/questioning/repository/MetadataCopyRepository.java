package fr.insee.survey.datacollectionmanagement.questioning.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.insee.survey.datacollectionmanagement.questioning.domain.MetadataCopy;

public interface MetadataCopyRepository extends JpaRepository<MetadataCopy, Long> {

}
