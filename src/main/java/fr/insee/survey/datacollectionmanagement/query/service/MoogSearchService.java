package fr.insee.survey.datacollectionmanagement.query.service;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.query.dto.MoogSearchDto;
import fr.insee.survey.datacollectionmanagement.view.domain.View;

@Service
public interface MoogSearchService {

    List<View> moogSearch(String field);

    List<MoogSearchDto> transformListViewToListMoogSearchDto(List<View> listView);

}
