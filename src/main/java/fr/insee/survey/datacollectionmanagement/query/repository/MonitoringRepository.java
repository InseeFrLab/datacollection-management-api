package fr.insee.survey.datacollectionmanagement.query.repository;

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
}