package fr.insee.survey.datacollectionmanagement.query.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import fr.insee.survey.datacollectionmanagement.query.dto.MoogFollowUpDto;
import fr.insee.survey.datacollectionmanagement.query.dto.MoogRowProgressDto;

@Repository
public class MonitoringRepository {

   @Autowired
    JdbcTemplate jdbcTemplate;

    final String progressQuery = "SELECT COUNT(survey_unit_id_su) as total,status,batch_num FROM\n" +
            "(SELECT DISTINCT ON (survey_unit_id_su) survey_unit_id_su,status,event_order,batch_num FROM (SELECT event_order, campaign_campaign_id, C.status, batch_num, survey_unit_id_su FROM (SELECT campaign_campaign_id, type as status, id_partitioning as batch_num, survey_unit_id_su  FROM\n" +
            "(select survey_unit_id_su, id_partitioning, questioning_id, type from questioning_event JOIN questioning ON questioning_event.questioning_id=questioning.id) As A\n" +
            "JOIN partitioning ON partitioning.id=A.id_partitioning\n" +
            "WHERE campaign_campaign_id=?) AS C JOIN event_order ON event_order.status = C.status\n" +
            "ORDER BY survey_unit_id_su,event_order DESC) AS G) AS M\n" +
            "GROUP BY status,batch_num";

    final String followUpQuery = "SELECT COUNT(A.survey_unit_id_su) as nb, id_partitioning as batch_num, freq FROM\n" +
            " (SELECT questioning.id, id_partitioning, survey_unit_id_su, campaign_campaign_id FROM questioning JOIN partitioning ON questioning.id_partitioning=partitioning.id\n" +
            " WHERE campaign_campaign_id=?) AS A LEFT JOIN(SELECT COUNT(type) as freq, survey_unit_id_su FROM(SELECT survey_unit_id_su, id_partitioning, questioning_id, type, campaign_campaign_id FROM\n" +
            " (select survey_unit_id_su, id_partitioning, questioning_id, type from questioning_event JOIN questioning ON questioning_event.questioning_id=questioning.id) As A\n" +
            " JOIN partitioning ON partitioning.id=A.id_partitioning\n" +
            " WHERE type='FOLLOWUP' AND campaign_campaign_id=?) AS B\n" +
            " GROUP BY survey_unit_id_su) AS G ON G.survey_unit_id_su=A.survey_unit_id_su\n" +
            " GROUP BY batch_num, freq";

    public List<MoogRowProgressDto> getProgress(String idCampaign) {
        List<MoogRowProgressDto> progress = jdbcTemplate.query(progressQuery, new RowMapper<MoogRowProgressDto>() {
            public MoogRowProgressDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                MoogRowProgressDto av = new MoogRowProgressDto();
                av.setBatchNum(Integer.parseInt(rs.getString("batch_num").substring(rs.getString("batch_num").length() - 1)));
                av.setStatus(rs.getString("status"));
                av.setTotal(Integer.parseInt(rs.getString("total")));

                return av;
            }
        }, new Object[]{idCampaign});

        return progress;
    }

    public List<MoogFollowUpDto> getFollowUp(String idCampaign) {
        List<MoogFollowUpDto> relance = jdbcTemplate.query(followUpQuery, new RowMapper<MoogFollowUpDto>() {
            public MoogFollowUpDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                MoogFollowUpDto rel = new MoogFollowUpDto();
                rel.setFreq(rs.getString("freq") != null ? Integer.parseInt(rs.getString("freq")) : 0);
                rel.setBatchNum(Integer.parseInt(rs.getString("batch_num").substring(rs.getString("batch_num").length() - 1)));
                rel.setNb(Integer.parseInt(rs.getString("nb")));

                return rel;
            }
        }, new Object[]{idCampaign, idCampaign});

        return relance;

    }
}
