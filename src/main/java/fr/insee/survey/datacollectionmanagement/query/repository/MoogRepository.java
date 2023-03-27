package fr.insee.survey.datacollectionmanagement.query.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

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

    final String extractionQuery = "SELECT survey_unit.id_su as id_su, "
            + "	survey_unit.id_contact as id_contact,"
            + "	survey_unit.lastname as lastname,"
            + "	survey_unit.firstname as firstname,"
            + "	survey_unit.address as address,	\r\n"
            + "	survey_unit.batch_num as batch_num,"
            + "	B.type	as status,"
            + "	B.date	as dateinfo "
            + " FROM survey_unit "+
            "			LEFT JOIN ( questioning_event LEFT JOIN uploads ON questioning_event.id_upload =uploads.id) AS B "+
            "			ON ( " +
            "     survey_unit.id_su=B.id_su AND " +
            "     survey_unit.id_contact=B.id_contact AND " +
            "     survey_unit.id_campaign=B.id_campaign) " +
            "			WHERE survey_unit.id_campaign=?";


    public List<MoogExtractionRowDto> getExtraction(String idCampaign) {
        List<MoogExtractionRowDto> extraction = jdbcTemplate.query(extractionQuery, new RowMapper<MoogExtractionRowDto>() {

            public MoogExtractionRowDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                MoogExtractionRowDto ev = new MoogExtractionRowDto();

                ev.setStatus(rs.getString("status"));
                ev.setDateInfo(Long.parseLong(rs.getString("dateinfo")));
                ev.setIdSu(rs.getString("id_su"));
                ev.setIdContact(rs.getString("id_contact"));
                ev.setLastname(rs.getString("lastname"));
                ev.setFirstname(rs.getString("firstname"));
                ev.setAddress(rs.getString("address"));
                ev.setBatchNumber(Integer.parseInt(rs.getString("batch_num")));

                return ev;
            }
        }, new Object[]{idCampaign});

        return extraction;
    }

    final String surveyUnitFollowUpQuery = "SELECT TABLE1.id_su, TABLE1.batch_num , CASE WHEN TABLE2.id_su IS NULL THEN 0 ELSE 1 END as PND FROM ("
            + "SELECT DISTINCT ON (survey_unit.id_su) "
            + "survey_unit.id_campaign, survey_unit.id_contact, survey_unit.id_su, address, batch_num, firstname, lastname, id_management_monitoring_info, dateinfo, status, id_upload "
            + "FROM survey_unit JOIN management_monitoring_info ON ( "
            + "survey_unit.id_su=management_monitoring_info.id_su AND "
            + "survey_unit.id_contact=management_monitoring_info.id_contact AND "
            + "survey_unit.id_campaign=management_monitoring_info.id_campaign) "
            + " WHERE (	survey_unit.id_campaign = ? AND  survey_unit.id_su NOT IN ("
            + "SELECT DISTINCT ON (survey_unit.id_su) survey_unit.id_su FROM survey_unit JOIN management_monitoring_info ON ( "
            + "survey_unit.id_su=management_monitoring_info.id_su AND "
            + "survey_unit.id_contact=management_monitoring_info.id_contact AND "
            + "survey_unit.id_campaign=management_monitoring_info.id_campaign) "
            + "WHERE status in ('VALINT','VALPAP','REFUSAL','WASTE','HC') AND survey_unit.id_campaign = ? )"
            + ")  ORDER BY survey_unit.id_su"
            + ") AS TABLE1 LEFT OUTER JOIN ("
            + "SELECT DISTINCT ON (survey_unit.id_su) survey_unit.id_su FROM survey_unit JOIN management_monitoring_info ON ( "
            + "survey_unit.id_su=management_monitoring_info.id_su AND "
            + "survey_unit.id_contact=management_monitoring_info.id_contact AND "
            + "survey_unit.id_campaign=management_monitoring_info.id_campaign) "
            + "WHERE status in ('PND') AND survey_unit.id_campaign = ?) AS TABLE2 ON TABLE1.id_su=TABLE2.id_su";

    public List<MoogExtractionRowDto> getSurveyUnitToFollowUp(String idCampaign) {

        List<MoogExtractionRowDto> followUp = jdbcTemplate.query(surveyUnitFollowUpQuery,
                new RowMapper<MoogExtractionRowDto>() {
                    public MoogExtractionRowDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                        MoogExtractionRowDto er = new MoogExtractionRowDto();
                        er.setIdSu(rs.getString("id_su"));
                        er.setPnd(rs.getInt("PND"));
                        er.setBatchNumber(rs.getInt("batch_num"));

                        return er;
                    }
                }, new Object[] { idCampaign, idCampaign, idCampaign });

        return followUp;

    }
}
