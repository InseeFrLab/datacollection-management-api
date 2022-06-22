package fr.insee.survey.datacollectionmanagement.questioning.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fr.insee.survey.datacollectionmanagement.questioning.domain.QuestioningAccreditation;

public interface QuestioningAccreditationRepository extends JpaRepository<QuestioningAccreditation, Long> {

    static final String QUERY_FIND_IDCONTACT =
        "select id_contact  from questioning_accreditation qa                                                                                   "
            + "     join questioning q                                                                                                          "
            + "     on q.id =qa.questioning_id                                                                                                  "
            + "     where q.id_partitioning = ?1                                                                                                ";

    static final String QUERY_FIND_IDPARTIONING =
        "select q.id_partitioning  from questioning q                                                                                           "
            + "join questioning_accreditation qa                                                                                                "
            + "on q.id =qa.questioning_id                                                                                                       "
            + "where qa.id_contact =?1                                                                                                          ";

    public List<QuestioningAccreditation> findByIdContact(String idContact);

    @Query(nativeQuery = true, value = QUERY_FIND_IDCONTACT)
    public List<String> findIdContactsByPartitionigAccredications(String idPartitioning);

    @Query(nativeQuery = true, value = QUERY_FIND_IDPARTIONING)
    public List<String> findIdPartitioningsByContactAccreditations(String idContact);

}
