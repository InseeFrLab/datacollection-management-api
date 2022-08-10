package fr.insee.survey.datacollectionmanagement.contact.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import fr.insee.survey.datacollectionmanagement.contact.domain.Contact;

public interface ContactRepository extends PagingAndSortingRepository<Contact, String> {

    static final String QUERY_ACCREDITATIONS_COPY =
        "select                                                                                                                                 "
            + "        c.*                                                                                                                      "
            + "from                                                                                                                             "
            + "        contact c                                                                                                                "
            + "left join accreditations_copy y                                                                                                  "
            + "on                                                                                                                               "
            + "        c.identifier = y.contact_identifier                                                                                      "
            + "where                                                                                                                            "
            + "        (:identifier is null or UPPER(c.identifier) = UPPER(cast(:identifier as text)))                                          "
            + "        and (:firstName is null or UPPER(c.first_name) = UPPER(cast( :firstName as text)))                                       "
            + "        and (:lastName is null or UPPER(c.last_name) = UPPER(cast(:lastName as text)))                                           "
            + "        and (:email is null or UPPER(c.email) = UPPER(cast( :email as text)))                                                    "
            + "        and (:idSu is null or UPPER(y.id_su) = UPPER(cast( :idSu as text)))                                                      "
            + "        and (:surveyUnitId is null or UPPER(y.survey_unit_id) = UPPER(cast( :surveyUnitId as text)))                             "
            + "        and (:companyName is null or UPPER(y.company_name) = UPPER(cast( :companyName as text)))                                 "
            + "        and (:source is null or UPPER(y.source_id) = UPPER(cast( :source as text)))                                              "
            + "        and (:period is null or UPPER(y.\"period\") = UPPER(cast( :period as text)))                                             ";

    static final String CLAUSE_YEAR = " and (:year is null or y.\"year\" = :year)                   ";

    @Query(nativeQuery = true, value = "SELECT *  FROM contact ORDER BY random() LIMIT 1")
    public Contact findRandomContact();

    @Query(nativeQuery = true, value = "SELECT identifier FROM contact TABLESAMPLE system_rows(1)")
    public String findRandomIdentifierContact();

    public List<Contact> findByLastNameIgnoreCase(String lastName);

    public List<Contact> findByFirstNameIgnoreCase(String firstName);

    public List<Contact> findByEmailIgnoreCase(String email);


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
