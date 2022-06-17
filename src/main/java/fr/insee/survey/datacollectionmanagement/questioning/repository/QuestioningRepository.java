package fr.insee.survey.datacollectionmanagement.questioning.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fr.insee.survey.datacollectionmanagement.questioning.domain.Questioning;

public interface QuestioningRepository extends JpaRepository<Questioning, Long> {

    public List<Questioning> findByIdPartitioning(String idPartitioning);

    @Query(nativeQuery = true, value = "select * from questioning q  where q.id_partitioning =?1 limit ?2")
    public List<Questioning> findByIdPartitioningLimit(String idPartitioning, int limit);
}
