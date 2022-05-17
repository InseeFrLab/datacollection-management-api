package fr.insee.survey.datacollectionmanagement.metadata.repository;

import fr.insee.survey.datacollectionmanagement.metadata.domain.Partitioning;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartitioningRepository extends JpaRepository<Partitioning, String> {
}
