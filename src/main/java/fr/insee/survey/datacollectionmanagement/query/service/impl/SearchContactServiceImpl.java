package fr.insee.survey.datacollectionmanagement.query.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.contact.domain.Contact;
import fr.insee.survey.datacollectionmanagement.contact.service.ContactService;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Partitioning;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Source;
import fr.insee.survey.datacollectionmanagement.metadata.service.SourceService;
import fr.insee.survey.datacollectionmanagement.metadata.service.SurveyService;
import fr.insee.survey.datacollectionmanagement.query.service.SearchContactService;
import fr.insee.survey.datacollectionmanagement.questioning.domain.Questioning;
import fr.insee.survey.datacollectionmanagement.questioning.domain.QuestioningAccreditation;
import fr.insee.survey.datacollectionmanagement.questioning.domain.SurveyUnit;
import fr.insee.survey.datacollectionmanagement.questioning.service.QuestioningAccreditationService;
import fr.insee.survey.datacollectionmanagement.questioning.service.QuestioningService;
import fr.insee.survey.datacollectionmanagement.questioning.service.SurveyUnitService;

@Service
public class SearchContactServiceImpl implements SearchContactService {

    @Autowired
    private ContactService contactService;

    @Autowired
    private QuestioningAccreditationService questioningAccreditationService;

    @Autowired
    private SurveyUnitService surveyUnitService;

    @Autowired
    private SourceService sourceService;

    @Autowired
    private SurveyService surveyService;

    @Autowired
    private QuestioningService questioningService;

    @Override
    public List<Contact> searchContactCrossDomain(
        String identifier,
        String lastName,
        String firstName,
        String email,
        String idSu,
        String surveyUnitId,
        String companyName,
        String source,
        String year,
        String period) {

        List<Contact> listFirstParameterNotNull = new ArrayList<>();
        List<Contact> listContactSurveyUnit = searchListContactSurveyUnit(idSu, surveyUnitId, companyName);
        List<Contact> listContactMetadata = searchListContactMetadata(source, year, period);

        if ( !StringUtils.isEmpty(identifier)) {
            listFirstParameterNotNull = Arrays.asList(contactService.findByIdentifier(identifier));

        }
        else if ( !StringUtils.isEmpty(lastName)) {
            listFirstParameterNotNull = contactService.findByLastName(lastName);
        }
        else if ( !StringUtils.isEmpty(firstName)) {
            listFirstParameterNotNull = contactService.findByFirstName(firstName);
        }
        else if ( !StringUtils.isEmpty(idSu)) {
            listFirstParameterNotNull = listContactSurveyUnit;
        }
        else if ( !StringUtils.isEmpty(surveyUnitId)) {
            listFirstParameterNotNull = listContactSurveyUnit;
        }
        else if ( !StringUtils.isEmpty(companyName)) {
            listFirstParameterNotNull = listContactSurveyUnit;
        }

        return listFirstParameterNotNull.stream().filter(c -> !StringUtils.isEmpty(lastName) ? c.getLastName().equalsIgnoreCase(lastName) : c != null)
            .filter(c -> !StringUtils.isEmpty(firstName) ? c.getFirstName().equalsIgnoreCase(firstName) : c != null)
            .filter(c -> !StringUtils.isEmpty(email) ? c.getEmail().equalsIgnoreCase(email) : c != null)
            .filter(c -> !listContactSurveyUnit.isEmpty() ? listContactSurveyUnit.contains(c) : c != null)
            .filter(c -> !listContactMetadata.isEmpty() ? listContactMetadata.contains(c) : c != null)
            .collect(Collectors.toList());
    }

    /**
     * Search for the list of qualified contacts who can respond for the survey units selected by the parameters:
     * @param idSu
     * @param surveyUnitId
     * @param companyName
     * @return List<Contact>
     */
    private List<Contact> searchListContactSurveyUnit(String idSu, String surveyUnitId, String companyName) {
        List<Contact> listContactSurveyUnit = new ArrayList<>();
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
            listContactSurveyUnit.addAll(findContactBySurveyUnit(surveyUnit));
        }
        return listContactSurveyUnit;
    }

    /**
     * earch for the list of qualified contacts who can respond for one survey unit
     * @param su
     * @return List<Contact>
     */
    private List<Contact> findContactBySurveyUnit(SurveyUnit su) {
        List<Contact> listReturn = new ArrayList<>();
        Set<QuestioningAccreditation> setAccreditations = questioningAccreditationService.findBySurveyUnit(su);
        for (QuestioningAccreditation qu : setAccreditations) {
            String ident = qu.getIdContact();
            listReturn.add(contactService.findByIdentifier(ident));
        }
        return listReturn;
    }

    /**
     * Search for the list of qualified contacts who can respond for the campaigns corresponding to the parameters
     * @param source
     * @param year
     * @param period
     * @return
     */
    private List<Contact> searchListContactMetadata(String idSource, String year, String period) {
        List<Contact> listContactMetadata = new ArrayList<>();
        List<Questioning> listQuestioning = new ArrayList<>();

        if ( !StringUtils.isEmpty(idSource)) {
            Source source = sourceService.findbyId(idSource);
            if (source != null) {
                List<Partitioning> listPart = new ArrayList<>();
                source.getSurveys().stream().forEach(s -> s.getCampaigns().stream().forEach(c -> listPart.addAll(c.getPartitionings())));
                listPart.stream().forEach(p -> listQuestioning.addAll(questioningService.fingByIdPartitioning(p.getId())));
                listQuestioning.stream().forEach(q -> q.getQuestioningAccreditations().stream()
                    .forEach(acc -> listContactMetadata.add(contactService.findByIdentifier(acc.getIdContact()))));

            }

        }
        else if ( !StringUtils.isEmpty(year)) {
            // listSu.addAll(surveyUnitService.findbySurveyUnitId(year));
        }
        else if ( !StringUtils.isEmpty(period)) {
            // listSu.addAll(surveyUnitService.findbyCompanyName(period));
        }

        // for (SurveyUnit surveyUnit : listSu) {
        // listContactMetadata.addAll(findContactBySurveyUnit(surveyUnit));
        // }
        return listContactMetadata;
    }

}
