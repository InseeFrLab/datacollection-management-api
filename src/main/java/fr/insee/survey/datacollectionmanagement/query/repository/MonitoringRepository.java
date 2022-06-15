package fr.insee.survey.datacollectionmanagement.query.repository;

import fr.insee.survey.datacollectionmanagement.query.dto.MoogFollowUpDto;
import fr.insee.survey.datacollectionmanagement.query.dto.MoogRowProgressDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class MonitoringRepository {

   @Autowired
    JdbcTemplate jdbcTemplate;

    final String progressQuery = "SELECT COUNT(survey_unit_id_su) as total, m.type as status, m.id_partitioning as batch_num FROM " +
            "(SELECT type, event_id, questioning_id, event_order, campaign_campaign_id, id_partitioning, survey_unit_id_su FROM " +
            "(SELECT type, questioning_id, event_id, event_order, id_partitioning, survey_unit_id_su FROM " +
            "(SELECT questioning_event.id as event_id,type, questioning_id, event_order, status " +
            " FROM questioning_event JOIN event_order ON event_order.status=questioning_event.type) AS A " +
            " JOIN questioning ON questioning.id=A.questioning_id) AS C " +
            " JOIN partitioning ON partitioning.id=C.id_partitioning " +
            " WHERE campaign_campaign_id=? " +
            " ORDER BY survey_unit_id_su, event_order DESC) AS m " +
            " GROUP BY status, batch_num";

    final String followUpQuery = "SELECT COUNT(survey_unit_id_su) as nb, batch_num, freq FROM\n" +
            "(SELECT COUNT(type) as freq, id_partitioning as batch_num, survey_unit_id_su FROM\n" +
            "(SELECT type, id_partitioning, questioning_id, campaign_campaign_id, survey_unit_id_su \n" +
            "FROM\n" +
            "(SELECT type, id_partitioning, questioning_id, survey_unit_id_su FROM questioning_event  \n" +
            "JOIN questioning ON  \n" +
            "questioning_event.questioning_id=questioning.id) AS A\n" +
            "JOIN partitioning ON partitioning.id=A.id_partitioning) AS C\n" +
            "WHERE (type='FOLLOWUP' AND campaign_campaign_id=?) \n" +
            "GROUP BY survey_unit_id_su, id_partitioning) AS G\n" +
            "GROUP BY batch_num, freq";

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
        }, new Object[]{idCampaign});

        return relance;

    }
}
