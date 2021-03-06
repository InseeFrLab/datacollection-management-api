package fr.insee.survey.datacollectionmanagement.contact.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import fr.insee.survey.datacollectionmanagement.contact.domain.Contact;

public interface ContactRepository extends PagingAndSortingRepository<Contact, String> {

    static final String QUERY_MULTI_CRITERIA =
        "select                                                                                                                                 "
            + "        c.*                                                                                                                      "
            + "from                                                                                                                             "
            + "        contact c                                                                                                                "
            + "join questioning_accreditation qa                                                                                                "
            + "        on                                                                                                                       "
            + "        c.identifier = qa.id_contact                                                                                             "
            + "join questioning q on                                                                                                            "
            + "        q.id = qa.questioning_id                                                                                                 "
            + "join survey_unit su                                                                                                              "
            + "on                                                                                                                               "
            + "        su.id_su = q.survey_unit_id_su                                                                                           "
            + "join partitioning p on                                                                                                           "
            + "        p.id = q.id_partitioning                                                                                                 "
            + "join campaign c2 on                                                                                                              "
            + "        p.campaign_campaign_id = c2.campaign_id                                                                                  "
            + "join survey y on                                                                                                                 "
            + "        y.id = c2.survey_id                                                                                                      "
            + "join \"source\" s2 on                                                                                                            "
            + "        s2.id_source = y.source_id_source                                                                                        "
            + "where                                                                                                                            "
            + "        (:identifier is null or c.identifier =cast(:identifier as text))                                                         "
            + "        and (:firstName is null or c.first_name =cast( :firstName as text))                                                      "
            + "        and (:lastName is null or c.last_name =cast(:lastName as text))                                                          "
            + "        and (:email is null or c.email =cast( :email as text))                                                                   "
            + "        and (:idSu is null or su.id_su =cast( :idSu as text))                                                                    "
            + "        and (:surveyUnitId is null or su.survey_unit_id =cast( :surveyUnitId as text))                                           "
            + "        and (:companyName is null or su.company_name =cast( :companyName as text))                                               "
            + "        and (:source is null or s2.id_source =cast( :source as text))                                                            "
            + "        and (:period is null or c2.\"period\" =cast( :period as text))                                                           ";

    static final String QUERY_ACCREDITATIONS_COPY =
        "select                                                                                                                                 "
            + "        c.*                                                                                                                      "
            + "from                                                                                                                             "
            + "        contact c                                                                                                                "
            + "join accreditations_copy y                                                                                                       "
            + "on                                                                                                                               "
            + "        c.identifier = y.contact_identifier                                                                                     "
            + "where                                                                                                                            "
            + "        (:identifier is null or c.identifier =cast(:identifier as text))                                                         "
            + "        and (:firstName is null or c.first_name =cast( :firstName as text))                                                      "
            + "        and (:lastName is null or c.last_name =cast(:lastName as text))                                                          "
            + "        and (:email is null or c.email =cast( :email as text))                                                                   "
            + "        and (:idSu is null or y.id_su =cast( :idSu as text))                                                                     "
            + "        and (:surveyUnitId is null or y.survey_unit_id =cast( :surveyUnitId as text))                                            "
            + "        and (:companyName is null or y.company_name =cast( :companyName as text))                                                "
            + "        and (:source is null or y.source_id =cast( :source as text))                                                             "
            + "        and (:period is null or y.\"period\" =cast( :period as text))                                                            ";

    static final String CLAUSE_YEAR = " and (:year is null or y.\"year\" = :year)                   ";

    @Query(nativeQuery = true, value = "SELECT *  FROM contact ORDER BY random() LIMIT 1")
    public Contact findRandomContact();

    @Query(nativeQuery = true, value = "SELECT identifier FROM contact TABLESAMPLE system_rows(1)")
    public String findRandomIdentifierContact();

    public List<Contact> findByLastNameIgnoreCase(String lastName);

    public List<Contact> findByFirstNameIgnoreCase(String firstName);

    public List<Contact> findByEmailIgnoreCase(String email);

    @Query(nativeQuery = true, value = QUERY_MULTI_CRITERIA)
    public Page<Contact> findContactMultiCriteria(
        @Param("identifier") String identifier,
        @Param("lastName") String lastName,
        @Param("firstName") String firstName,
        @Param("email") String email,
        @Param("idSu") String idSu,
        @Param("surveyUnitId") String surveyUnitId,
        @Param("companyName") String companyName,
        @Param("source") String source,
        @Param("period") String period,
        Pageable pageable);

    @Query(nativeQuery = true, value = QUERY_MULTI_CRITERIA + CLAUSE_YEAR)
    public Page<Contact> findContactMultiCriteriaYear(
        @Param("identifier") String identifier,
        @Param("lastName") String lastName,
        @Param("firstName") String firstName,
        @Param("email") String email,
        @Param("idSu") String idSu,
        @Param("surveyUnitId") String surveyUnitId,
        @Param("companyName") String companyName,
        @Param("source") String source,
        @Param("year") Integer year,
        @Param("period") String period,
        Pageable pageable);

    @Query(nativeQuery = true, value = QUERY_ACCREDITATIONS_COPY)
    public Page<Contact> findContactMultiCriteriaAccreditationsCopy(
        @Param("identifier") String identifier,
        @Param("lastName") String lastName,
        @Param("firstName") String firstName,
        @Param("email") String email,
        @Param("idSu") String idSu,
        @Param("surveyUnitId") String surveyUnitId,
        @Param("companyName") String companyName,
        @Param("source") String source,
        @Param("period") String period,
        Pageable pageable);
    
    @Query(nativeQuery = true, value = QUERY_ACCREDITATIONS_COPY+CLAUSE_YEAR)
    public Page<Contact> findContactMultiCriteriaAccreditationsCopyYear(
        @Param("identifier") String identifier,
        @Param("lastName") String lastName,
        @Param("firstName") String firstName,
        @Param("email") String email,
        @Param("idSu") String idSu,
        @Param("surveyUnitId") String surveyUnitId,
        @Param("companyName") String companyName,
        @Param("source") String source,
        @Param("year") Integer year,
        @Param("period") String period,
        Pageable pageable);

}
