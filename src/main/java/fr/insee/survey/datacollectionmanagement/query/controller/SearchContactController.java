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

import fr.insee.survey.datacollectionmanagement.contact.domain.Contact;
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

    @GetMapping(path = "contacts/search")
    @Operation(summary = "V1: Multi-criteria search with the separate domains option")
    public ResponseEntity<?> search(
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
            "Search contact V1: identifier ={}, lastName={}, firstName={}, email={}, idSu={}, surveyUnitId={}, companyName={}, source={}, year={}, period={}, pageNo={}, pageSize={} ",
            identifier, lastName, firstName, email, idSu, surveyUnitId, companyName, source, year, period, pageNo, pageSize);
        List<Contact> listContacts =
            searchContactService.searchContactCrossDomain(identifier, lastName, firstName, email, idSu, surveyUnitId, companyName, source, year, period);

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        int start = (int) pageable.getOffset();
        int end = (int) ((start + pageable.getPageSize()) > listContacts.size() ? listContacts.size() : (start + pageable.getPageSize()));

        if (start <= end) {
            Page<SearchContactDto> page =
                new PageImpl<SearchContactDto>(searchContactService.transformListContactDaoToDto(listContacts.subList(start, end)), pageable,
                    listContacts.size());
            return new ResponseEntity<>(page, HttpStatus.OK);

        }
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

    @GetMapping(path = "contacts/searchV2")
    @Operation(summary = "V2: Multi-criteria search using view")
    public ResponseEntity<?> searchV2(
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
            "Search contact V2: identifier ={}, lastName={}, firstName={}, email={}, idSu={}, surveyUnitId={}, companyName={}, source={}, year={}, period={}, pageNo={}, pageSize={} ",
            identifier, lastName, firstName, email, idSu, surveyUnitId, companyName, source, year, period, pageNo, pageSize);

        Pageable pageable = PageRequest.of(pageNo, pageSize);

        List<View> listView =
            searchContactService.searchContactV2CrossDomain(identifier, lastName, firstName, email, idSu, surveyUnitId, companyName, source, year, period,
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

    @GetMapping(path = "contacts/searchV3")
    @Operation(summary = "V3: Multi-criteria search in contact domain (copy of accreditations and metadata into contacts)")
    public ResponseEntity<?> searchV3(
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
            "Search contact V3: identifier ={}, lastName={}, firstName={}, email={}, idSu={}, surveyUnitId={}, companyName={}, source={}, year={}, period={}, pageNo={}, pageSize={} ",
            identifier, lastName, firstName, email, idSu, surveyUnitId, companyName, source, year, period, pageNo, pageSize);

        Pageable pageable = PageRequest.of(pageNo, pageSize);

        Page<Contact> pageContacts =
            searchContactService.searchContactV3CrossDomain(identifier, lastName, firstName, email, idSu, surveyUnitId, companyName, source, year, period,
                pageable);

        Page<SearchContactDto> page =
            new PageImpl<SearchContactDto>(searchContactService.transformPageContactDaoToDto(pageContacts), pageable, pageContacts.getTotalElements());
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

}
