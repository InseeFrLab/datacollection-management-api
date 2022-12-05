package fr.insee.survey.datacollectionmanagement.metadata.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.insee.survey.datacollectionmanagement.metadata.domain.Owner;

public interface OwnerRepository extends JpaRepository<Owner, String> {
}
