package fr.insee.survey.datacollectionmanagement.questioning.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fr.insee.survey.datacollectionmanagement.metadata.domain.Partitioning;
import fr.insee.survey.datacollectionmanagement.questioning.domain.SurveyUnit;

public interface SurveyUnitRepository extends JpaRepository<SurveyUnit, String> {

    String QUERY_ID_SU = "select "
        + "        id_contact "
        + "from "
        + "        questioning_accreditation qa "
        + "join questioning q  "
        + "on "
        + "        q.id = qa.questioning_id "
        + "where "
        + "        q.survey_unit_id_su =?";
    
    String QUERY_SURVEY_UNIT_ID = "select "
        + "        id_contact "
        + "from "
        + "        questioning_accreditation qa "
        + "join questioning q  "
        + "on "
        + "        q.id = qa.questioning_id "
        + "join survey_unit su on "
        + "        su.id_su = q.survey_unit_id_su "
        + "where "
        + "        su.survey_unit_id =?";
    


    String QUERY_COMPANY_NAME = "select "
        + "        id_contact "
        + "from "
        + "        questioning_accreditation qa "
        + "join questioning q  "
        + "on "
        + "        q.id = qa.questioning_id "
        + "join survey_unit su on "
        + "        su.id_su = q.survey_unit_id_su "
        + "where "
        + "         su.company_name =cast(?1 as TEXT) ";

    public List<SurveyUnit> findAllBySurveyUnitId(String surveyUnitId);

    public List<SurveyUnit> findByCompanyNameIgnoreCase(String companyName);

    @Query(nativeQuery=true, value=QUERY_SURVEY_UNIT_ID)
    
    
    public List<String> findIdContactsBySurveyUnitId(String surveyUnitId);
    
    @Query(nativeQuery=true, value=QUERY_ID_SU)
    public List<String> findIdContactsByIdSu(String idSu);

    @Query(nativeQuery=true, value=QUERY_COMPANY_NAME)
    public List<String> findIdContactsByCompanyName(String companyName);
    
    @Query(nativeQuery = true, value = "SELECT *  FROM survey_unit ORDER BY random() LIMIT 1")
    public SurveyUnit findRandomSurveyUnit();
}
