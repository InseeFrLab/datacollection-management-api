package fr.insee.survey.datacollectionmanagement.query.service;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.contact.domain.AccreditationsCopy;
import fr.insee.survey.datacollectionmanagement.query.dto.MoogSearchDto;
import fr.insee.survey.datacollectionmanagement.view.domain.View;

@Service
public interface MoogSearchService {

    List<View> moogSearchV2(String field);

    List<AccreditationsCopy> moogSearchV3(String field);

    List<MoogSearchDto> transformListViewToListMoogSearchDto(List<View> listView);

    List<MoogSearchDto> transformListAccreditationsCopyToListMoogSearchDto(List<AccreditationsCopy> listAccreditationsCopy);

}
