package fr.insee.survey.datacollectionmanagement.query.repository;

import fr.insee.survey.datacollectionmanagement.query.dto.MoogQuestioningEventDto;
import fr.insee.survey.datacollectionmanagement.query.dto.MoogSearchDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
}
