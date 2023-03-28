package fr.insee.survey.datacollectionmanagement.query.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import fr.insee.survey.datacollectionmanagement.contact.domain.Address;
import fr.insee.survey.datacollectionmanagement.contact.service.AddressService;
import fr.insee.survey.datacollectionmanagement.contact.service.impl.AddressServiceImpl;
import fr.insee.survey.datacollectionmanagement.query.dto.MoogExtractionRowDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import fr.insee.survey.datacollectionmanagement.query.dto.MoogQuestioningEventDto;

@Repository
public class MoogRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    AddressService addressService;

    final String getEventsQuery = "SELECT qe.id, extract(EPOCH from date)*1000 as date_timestamp, type, survey_unit_id_su, campaign_id "
    + " FROM questioning_event qe join questioning q on qe.questioning_id=q.id join partitioning p on q.id_partitioning=p.id "
    + " WHERE survey_unit_id_su=? AND campaign_id=? ";


    public List<MoogQuestioningEventDto> getEventsByIdSuByCampaign(String idCampaign, String idSu) {
        List<MoogQuestioningEventDto> progress = jdbcTemplate.query(getEventsQuery, new RowMapper<MoogQuestioningEventDto>() {
            public MoogQuestioningEventDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                MoogQuestioningEventDto moogEvent = new MoogQuestioningEventDto();
                moogEvent.setIdManagementMonitoringInfo(rs.getString("id"));
                moogEvent.setStatus(rs.getString("type"));
                moogEvent.setDateInfo(rs.getLong("date_timestamp"));
                return moogEvent;
            }
        }, new Object[]{idSu,idCampaign});

        return progress;
    }

    final String extractionQuery = " SELECT id_su,identifier as id_contact,first_name as firstname,last_name as lastname,address_id as address,date as dateinfo,type as status, batch_num FROM "
            + " (SELECT id,campaign_id,A.id_su,A.identifier,first_name,last_name,address_id, id_partitioning AS batch_num FROM (SELECT campaign_id,id_su,contact.identifier,first_name,last_name,address_id "
              +  "    FROM view "
               + "      LEFT JOIN contact ON contact.identifier=view.identifier WHERE campaign_id=?  ) As A LEFT JOIN questioning ON A.id_su=questioning.survey_unit_id_su) As B LEFT JOIN questioning_event on B.id=questioning_event.questioning_id"
    ;




    public List<MoogExtractionRowDto> getExtraction(String idCampaign) {
        List<MoogExtractionRowDto> extraction = jdbcTemplate.query(extractionQuery, new RowMapper<MoogExtractionRowDto>() {

            public MoogExtractionRowDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                MoogExtractionRowDto ev = new MoogExtractionRowDto();

                ev.setAddress("addresse non connue");
                Optional<Address> address = addressService.findById(rs.getLong("address"));

                ev.setStatus(rs.getString("status"));
                ev.setDateInfo(rs.getString("dateinfo"));
                ev.setIdSu(rs.getString("id_su"));
                ev.setIdContact(rs.getString("id_contact"));
                ev.setLastname(rs.getString("lastname"));
                ev.setFirstname(rs.getString("firstname"));
                if(address.isPresent()){
                ev.setAddress(address.get().toStringMoog());}

                ev.setBatchNumber(rs.getString("batch_num"));

                return ev;
            }
        }, new Object[]{idCampaign});

        return extraction;
    }

    final String surveyUnitFollowUpQuery = " SELECT DISTINCT ON (id_su) id_su,batch_num, CASE WHEN type in ('PND') THEN 1 ELSE 0 END as PND FROM (SELECT id_su, identifier, id, id_partitioning as batch_num FROM (SELECT id_su,identifier "+
            " FROM public.view WHERE campaign_id=?)AS A LEFT JOIN questioning ON questioning.survey_unit_id_su=A.id_su) AS B "+
            " LEFT JOIN questioning_event ON B.id=questioning_event.questioning_id "+
            " WHERE id_su not in (SELECT DISTINCT ON (id_su) id_su FROM (SELECT id_su, identifier, id, id_partitioning as batch_num FROM (SELECT id_su,identifier FROM public.view "+
            " WHERE campaign_id=?)AS A LEFT JOIN questioning ON questioning.survey_unit_id_su=A.id_su) AS B "+
    " LEFT JOIN questioning_event ON B.id=questioning_event.questioning_id "+
    " WHERE type IN ('VALINT','VALPAP','HC','REFUSAL','WASTE')) ORDER BY id_su,pnd DESC";
    ;

    public List<MoogExtractionRowDto> getSurveyUnitToFollowUp(String idCampaign) {

        List<MoogExtractionRowDto> followUp = jdbcTemplate.query(surveyUnitFollowUpQuery,
                new RowMapper<MoogExtractionRowDto>() {
                    public MoogExtractionRowDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                        MoogExtractionRowDto er = new MoogExtractionRowDto();
                        er.setIdSu(rs.getString("id_su"));
                        er.setPnd(rs.getInt("PND"));
                        er.setBatchNumber(rs.getString("batch_num"));

                        return er;
                    }
                }, new Object[] { idCampaign, idCampaign });

        return followUp;

    }
}
