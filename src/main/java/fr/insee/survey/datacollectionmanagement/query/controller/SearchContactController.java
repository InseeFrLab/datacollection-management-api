package fr.insee.survey.datacollectionmanagement.query.controller;

import java.util.List;

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

@RestController
@CrossOrigin
public class SearchContactController {

    @Autowired
    private SearchContactService searchContactService;

    @GetMapping(path = "contacts/search")
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

        List<Contact> listContacts =
            searchContactService.searchContactCrossDomain(identifier, lastName, firstName, email, idSu, surveyUnitId, companyName, source, year, period);

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        int start = (int) pageable.getOffset();
        int end = (int) ((start + pageable.getPageSize()) > listContacts.size() ? listContacts.size() : (start + pageable.getPageSize()));

        if ( !listContacts.isEmpty() && start < end) {
            Page<SearchContactDto> page =
                new PageImpl<SearchContactDto>(searchContactService.transformListContactDaoToDto(listContacts.subList(start, end)), pageable,
                    listContacts.size());
            return new ResponseEntity<>(page, HttpStatus.OK);

        }
        else
            return new ResponseEntity<>("0 contact found ", HttpStatus.NOT_FOUND);

    }

}
