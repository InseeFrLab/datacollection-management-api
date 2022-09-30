package fr.insee.survey.datacollectionmanagement.query.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import fr.insee.survey.datacollectionmanagement.query.dto.SearchContactDto;
import fr.insee.survey.datacollectionmanagement.view.domain.View;

public interface SearchContactService {

    /**
     * Search contact according to diffeent parameters
     * @param identifier
     * @param lastName
     * @param firstName
     * @param email
     * @param idSu
     * @param identificationCode
     * @param identificationName
     * @param source
     * @param year
     * @param period
     * @return
     */
    List<View> searchContactCrossDomain(
        String identifier,
        String lastName,
        String firstName,
        String email,
        String idSu,
        String identificationCode,
        String identificationName,
        String source,
        String year,
        String period,
        Pageable pageable);

    List<SearchContactDto> transformListViewDaoToDto(List<View> subList);

}
