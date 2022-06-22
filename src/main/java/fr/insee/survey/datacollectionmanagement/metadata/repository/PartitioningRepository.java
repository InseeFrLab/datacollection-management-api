package fr.insee.survey.datacollectionmanagement.metadata.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fr.insee.survey.datacollectionmanagement.metadata.domain.Partitioning;

public interface PartitioningRepository extends JpaRepository<Partitioning, String> {
    
    @Query(nativeQuery=true, value="SELECT *  FROM partitioning ORDER BY random() LIMIT 1")
    public Partitioning findRandomPartitioning();

}
