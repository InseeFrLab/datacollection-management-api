package fr.insee.survey.datacollectionmanagement.query.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.insee.survey.datacollectionmanagement.contact.domain.Contact;
import fr.insee.survey.datacollectionmanagement.contact.service.ContactService;
import fr.insee.survey.datacollectionmanagement.questioning.domain.QuestioningAccreditation;
import fr.insee.survey.datacollectionmanagement.questioning.domain.SurveyUnit;
import fr.insee.survey.datacollectionmanagement.questioning.service.QuestioningAccreditationService;
import fr.insee.survey.datacollectionmanagement.questioning.service.SurveyUnitService;

@RestController
@CrossOrigin
public class SearchContactController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private QuestioningAccreditationService questioningAccreditationService;

    @Autowired
    private SurveyUnitService surveyUnitService;

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
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(defaultValue = "identifier") String sortBy) {

        List<Contact> initList = new ArrayList<>();
        List<Contact> listC = new ArrayList<>();
        List<SurveyUnit> listSu = new ArrayList<>();

        if ( !StringUtils.isEmpty(idSu)) {
            listSu.add(surveyUnitService.findbyId(idSu));
        }
        else if ( !StringUtils.isEmpty(surveyUnitId)) {
            listSu.addAll(surveyUnitService.findbySurveyUnitId(surveyUnitId));
        }
        else if ( !StringUtils.isEmpty(companyName)) {
            listSu.addAll(surveyUnitService.findbyCompanyName(companyName));
        }
        
        for (SurveyUnit surveyUnit : listSu) {
            listC.addAll(findContactBySurveyUnit(surveyUnit));
        }
        
        final List<Contact> listContactsSu = listC;

        if ( !StringUtils.isEmpty(identifier)) {
            try {
                initList = Arrays.asList(contactService.findByIdentifier(identifier));
            }
            catch (NoSuchElementException e) {
                return new ResponseEntity<>("0 contact found ", HttpStatus.NOT_FOUND);
            }
        }
        else if ( !StringUtils.isEmpty(lastName)) {
            initList = contactService.findByLastName(lastName);
        }
        else if ( !StringUtils.isEmpty(firstName)) {
            initList = contactService.findByFirstName(firstName);
        }
        else if ( !StringUtils.isEmpty(idSu)) {
            initList = listContactsSu;
        }
        else if ( !StringUtils.isEmpty(surveyUnitId)) {
            initList = listContactsSu;
        }
        else if ( !StringUtils.isEmpty(companyName)) {
            initList = listContactsSu;
        }
        
        if ( !initList.isEmpty())
            return new ResponseEntity<>(initList.stream().filter(c -> !StringUtils.isEmpty(lastName) ? c.getLastName().equalsIgnoreCase(lastName) : c != null)
                .filter(c -> !StringUtils.isEmpty(firstName) ? c.getFirstName().equalsIgnoreCase(firstName) : c != null)
                .filter(c -> !StringUtils.isEmpty(email) ? c.getEmail().equalsIgnoreCase(email) : c != null)
                .filter(c -> !listContactsSu.isEmpty() ? listContactsSu.contains(c) : c != null).collect(Collectors.toList()), HttpStatus.OK);
        else
            return new ResponseEntity<>("0 contact found ", HttpStatus.NOT_FOUND);

    }

    private List<Contact> findContactBySurveyUnit(SurveyUnit su) {
        List<Contact> listReturn = new ArrayList<>();
        Set<QuestioningAccreditation> setAccreditations = questioningAccreditationService.findBySurveyUnit(su);
        for (QuestioningAccreditation qu : setAccreditations) {
            String ident = qu.getIdContact();
            listReturn.add(contactService.findByIdentifier(ident));
        }
        return listReturn;
    }

}
