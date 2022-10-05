package fr.insee.survey.datacollectionmanagement.query.service;

import java.util.List;

import fr.insee.survey.datacollectionmanagement.query.dto.MoogQuestioningEventDto;
import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.query.dto.MoogSearchDto;
import fr.insee.survey.datacollectionmanagement.view.domain.View;

@Service
public interface MoogService {

    List<View> moogSearch(String field);

    List<MoogSearchDto> transformListViewToListMoogSearchDto(List<View> listView);

    List<MoogQuestioningEventDto> getMoogEvents(String Campaign, String idSu);

}
