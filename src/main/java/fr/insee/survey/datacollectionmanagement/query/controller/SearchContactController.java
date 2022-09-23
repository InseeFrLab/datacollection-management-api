package fr.insee.survey.datacollectionmanagement.query.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.insee.survey.datacollectionmanagement.constants.Constants;
import fr.insee.survey.datacollectionmanagement.query.dto.SearchContactDto;
import fr.insee.survey.datacollectionmanagement.query.service.SearchContactService;
import fr.insee.survey.datacollectionmanagement.view.domain.View;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@CrossOrigin
public class SearchContactController {

    static final Logger LOGGER = LoggerFactory.getLogger(SearchContactController.class);

    @Autowired
    private SearchContactService searchContactService;

    @GetMapping(path = Constants.API_CONTACTS_SEARCH)
    @Operation(summary = "Multi-criteria search ontacts")
    public ResponseEntity<?> searchContacts(
        @RequestParam(required = false) String identifier,
        @RequestParam(required = false) String lastName,
        @RequestParam(required = false) String firstName,
        @RequestParam(required = false) String email,
        @RequestParam(required = false) String idSu,
        @RequestParam(required = false) String surveyUnitId,
        @RequestParam(required = false) String companyName,
        @RequestParam(required = false) String source,
        @RequestParam(required = false) String year,
        @RequestParam(required = false) String period,
        @RequestParam(defaultValue = "0") Integer pageNo,
        @RequestParam(defaultValue = "10") Integer pageSize) {
        
        LOGGER.info(
            "Search contact: identifier = {}, lastName= {}, firstName= {}, email= {}, idSu= {}, surveyUnitId= {}, companyName= {}, source= {}, year= {}, period= {}, pageNo= {}, pageSize= {} ",
            identifier, lastName, firstName, email, idSu, surveyUnitId, companyName, source, year, period, pageNo, pageSize);

        Pageable pageable = PageRequest.of(pageNo, pageSize);

        List<View> listView =
            searchContactService.searchContactCrossDomain(identifier, lastName, firstName, email, idSu, surveyUnitId, companyName, source, year, period,
                pageable);
        int start = (int) pageable.getOffset();
        int end = (int) ((start + pageable.getPageSize()) > listView.size() ? listView.size() : (start + pageable.getPageSize()));

        if (start <= end) {
            Page<SearchContactDto> page =
                new PageImpl<SearchContactDto>(searchContactService.transformListViewDaoToDto(listView.subList(start, end)), pageable, listView.size());
            return new ResponseEntity<>(page, HttpStatus.OK);

        }
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

}
