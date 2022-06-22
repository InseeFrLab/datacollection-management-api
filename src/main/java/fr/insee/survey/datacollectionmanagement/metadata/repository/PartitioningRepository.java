package fr.insee.survey.datacollectionmanagement.metadata.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fr.insee.survey.datacollectionmanagement.metadata.domain.Partitioning;

public interface PartitioningRepository extends JpaRepository<Partitioning, String> {

    static final String FIND_ID_PARTITIONNING =
        "select                                                                                                            "
            + "        p.id                                                                                                "
            + "from                                                                                                        "
            + "        partitioning p                                                                                      "
            + "join campaign c                                                                                             "
            + "on                                                                                                          "
            + "        c.campaign_id = p.campaign_campaign_id                                                              "
            + "join survey su on                                                                                           "
            + "        su.id = c.survey_id                                                                                 "
            + "join \"source\" s2 on                                                                                       "
            + "        s2.id_source = su.source_id_source                                                                  ";

    static final String QUERY_SOURCE_SURVEY_CAMPAIGN = FIND_ID_PARTITIONNING + " where s2.id_source = ?1 and su.\"year\" = cast(?2  as integer) and c.\"period\" = ?3  ";

    static final String QUERY_SOURCE = FIND_ID_PARTITIONNING + " where s2.id_source = ?1 ";

    static final String QUERY_YEAR = FIND_ID_PARTITIONNING + " where su.\"year\" = cast(?1 as integer) ";

    static final String QUERY_PERIOD = FIND_ID_PARTITIONNING + " where c.\"period\" = ?1 ";

    @Query(nativeQuery = true, value = "SELECT *  FROM partitioning ORDER BY random() LIMIT 1")
    public Partitioning findRandomPartitioning();

    @Query(nativeQuery = true, value = QUERY_SOURCE_SURVEY_CAMPAIGN)
    public List<String> findIdPartitioningBySourceIdYearPeriod(String sourceId, String year, String period);

    @Query(nativeQuery = true, value = QUERY_SOURCE)
    public List<String> findIdPartitioningBySourceId(String sourceId);

    @Query(nativeQuery = true, value = QUERY_YEAR)
    public List<String> findIdPartitioningByYear(String year);

    @Query(nativeQuery = true, value = QUERY_PERIOD)
    public List<String> findIdPartitioningByPeriod(String period);

}
