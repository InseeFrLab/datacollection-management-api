package fr.insee.survey.datacollectionmanagement.query.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.contact.domain.Contact;
import fr.insee.survey.datacollectionmanagement.contact.service.ContactService;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Campaign;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Partitioning;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Source;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Survey;
import fr.insee.survey.datacollectionmanagement.metadata.service.CampaignService;
import fr.insee.survey.datacollectionmanagement.metadata.service.PartitioningService;
import fr.insee.survey.datacollectionmanagement.metadata.service.SourceService;
import fr.insee.survey.datacollectionmanagement.metadata.service.SurveyService;
import fr.insee.survey.datacollectionmanagement.query.dto.SearchContactDto;
import fr.insee.survey.datacollectionmanagement.query.service.SearchContactService;
import fr.insee.survey.datacollectionmanagement.questioning.domain.Questioning;
import fr.insee.survey.datacollectionmanagement.questioning.domain.QuestioningAccreditation;
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

    @Autowired
    private PartitioningService partitioningService;

    @Autowired
    private CampaignService campaignService;

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

        List<Contact> listContact = new ArrayList<>();
        boolean alwaysEmpty = true;

        if ( !StringUtils.isEmpty(identifier)) {
            listContact = Arrays.asList(contactService.findByIdentifier(identifier));
            alwaysEmpty = false;
        }

        if ( !StringUtils.isEmpty(lastName)) {
            if (listContact.isEmpty() && alwaysEmpty) {
                listContact.addAll(contactService.findByLastName(lastName));
                alwaysEmpty = false;
            }
            else
                listContact = listContact.stream().filter(c -> lastName.equalsIgnoreCase(c.getLastName())).collect(Collectors.toList());
        }

        if ( !StringUtils.isEmpty(firstName)) {
            if (listContact.isEmpty() && alwaysEmpty) {
                listContact.addAll(contactService.findByFirstName(firstName));
                alwaysEmpty = false;
            }
            else
                listContact = listContact.stream().filter(c -> firstName.equalsIgnoreCase(c.getFirstName())).collect(Collectors.toList());
        }

        if ( !StringUtils.isEmpty(email)) {
            if (listContact.isEmpty() && alwaysEmpty) {
                listContact.addAll(contactService.findByEmail(email));
                alwaysEmpty = false;
            }
            else
                listContact = listContact.stream().filter(c -> email.equalsIgnoreCase(c.getEmail())).collect(Collectors.toList());
        }

        if ( !StringUtils.isEmpty(idSu)) {
            if (listContact.isEmpty() && alwaysEmpty) {
                listContact.addAll(findContactsByIdSu(idSu));
                alwaysEmpty = false;
            }
            else
                listContact = listContact.stream().filter(c -> findContactsByIdSu(idSu).contains(c)).collect(Collectors.toList());
        }

        if ( !StringUtils.isEmpty(surveyUnitId)) {
            if (listContact.isEmpty() && alwaysEmpty) {
                listContact.addAll(findContactsBySurveyUnitId(surveyUnitId));
                alwaysEmpty = false;
            }
            else {
                listContact = listContact.stream().filter(c -> findContactsBySurveyUnitId(surveyUnitId).contains(c)).collect(Collectors.toList());

            }
        }

        if ( !StringUtils.isEmpty(companyName)) {
            if (listContact.isEmpty() && alwaysEmpty) {
                listContact.addAll(findContactsByCompanyName(companyName));
                alwaysEmpty = false;
            }
            else {
                listContact = listContact.stream().filter(c -> findContactsByCompanyName(companyName).contains(c)).collect(Collectors.toList());

            }
        }

        boolean kCampaign = false;

        if ( !StringUtils.isEmpty(source) && !StringUtils.isEmpty(year) && !StringUtils.isEmpty(period)) {
            kCampaign = true;
            if (listContact.isEmpty() && alwaysEmpty) {
                List<Partitioning> listPart = new ArrayList<>();
                campaignService.findbyPeriod(period).stream()
                    .filter(c -> c.getSurvey().getYear() == Integer.parseInt(year) & c.getSurvey().getSource().getIdSource().equals(source))
                    .collect(Collectors.toList()).forEach(camp -> listPart.addAll(camp.getPartitionings()));
                for (Partitioning part : listPart)
                    listContact.addAll(findContactByPartitioning(part));

                alwaysEmpty = false;
            }
            else {

                List<Contact> listContactCopy = List.copyOf(listContact);
                for (Contact c : listContactCopy) {
                    boolean hasHabYear = false;

                    List<QuestioningAccreditation> listAccreditations = questioningAccreditationService.findByIdContact(c.getIdentifier());
                    List<Partitioning> listPart = new ArrayList<>();
                    campaignService.findbyPeriod(period).stream()
                        .filter(camp -> camp.getSurvey().getYear() == Integer.parseInt(year) & camp.getSurvey().getSource().getIdSource().equals(source))
                        .collect(Collectors.toList()).forEach(camp -> listPart.addAll(camp.getPartitionings()));

                    for (QuestioningAccreditation acc : listAccreditations) {
                        for (Partitioning part : listPart) {
                            if (acc.getQuestioning().getIdPartitioning().equals(part.getId())) {
                                hasHabYear = true;
                                break;
                            }
                        }
                    }
                    if ( !hasHabYear) {
                        listContact.remove(c);
                    }

                }

            }
        }

        if ( !StringUtils.isEmpty(source) && !kCampaign) {
            Source s = sourceService.findbyId(source);
            List<Partitioning> listPart = new ArrayList<>();
            if (s != null) {
                s.getSurveys().stream().forEach(so -> so.getCampaigns().stream().forEach(camp -> listPart.addAll(camp.getPartitionings())));
            }
            if (listContact.isEmpty() && alwaysEmpty) {
                for (Partitioning part : listPart)
                    listContact.addAll(findContactByPartitioning(part));

                alwaysEmpty = false;
            }
            else {
                List<Contact> listContactCopy = List.copyOf(listContact);
                for (Contact c : listContactCopy) {
                    List<QuestioningAccreditation> listAccreditations = questioningAccreditationService.findByIdContact(c.getIdentifier());
                    sourceService.findbyId(source).getSurveys().stream()
                        .forEach(su -> su.getCampaigns().stream().forEach(camp -> listPart.addAll(camp.getPartitionings())));
                    boolean hasHabYear = false;

                    for (QuestioningAccreditation acc : listAccreditations) {
                        for (Partitioning part : listPart) {
                            if (acc.getQuestioning().getIdPartitioning().equals(part.getId())) {
                                hasHabYear = true;
                                break;
                            }
                        }
                    }
                    if ( !hasHabYear) {
                        listContact.remove(c);
                    }

                }
            }
        }

        if ( !StringUtils.isEmpty(year) && !kCampaign) {
            if (listContact.isEmpty() && alwaysEmpty) {
                List<Survey> listSurveys = surveyService.findbyYear(Integer.parseInt(year));
                List<Partitioning> listPart = new ArrayList<>();
                if ( !listSurveys.isEmpty()) {
                    listSurveys.stream().forEach(so -> so.getCampaigns().stream().forEach(camp -> listPart.addAll(camp.getPartitionings())));
                }
                for (Partitioning part : listPart)
                    listContact.addAll(findContactByPartitioning(part));

                alwaysEmpty = false;
            }
            else {

                List<Contact> listContactCopy = List.copyOf(listContact);
                for (Contact c : listContactCopy) {
                    List<QuestioningAccreditation> listAccreditations = questioningAccreditationService.findByIdContact(c.getIdentifier());
                    List<Partitioning> listPart = new ArrayList<>();
                    surveyService.findbyYear(Integer.parseInt(year)).stream().collect(Collectors.toList())
                        .forEach(s -> s.getCampaigns().stream().forEach(camp -> listPart.addAll(camp.getPartitionings())));
                    boolean hasHabYear = false;

                    for (QuestioningAccreditation acc : listAccreditations) {
                        for (Partitioning part : listPart) {
                            if (acc.getQuestioning().getIdPartitioning().equals(part.getId())) {
                                hasHabYear = true;
                                break;
                            }
                        }
                    }
                    if ( !hasHabYear) {
                        listContact.remove(c);
                    }

                }

            }

        }

        if ( !StringUtils.isEmpty(period) && !kCampaign) {
            if (listContact.isEmpty() && alwaysEmpty) {
                List<Campaign> listCampaigns = campaignService.findbyPeriod(period);
                List<Partitioning> listPart = new ArrayList<>();
                if ( !listCampaigns.isEmpty()) {
                    listCampaigns.stream().forEach(camp -> listPart.addAll(camp.getPartitionings()));
                }
                for (Partitioning part : listPart)
                    listContact.addAll(findContactByPartitioning(part));

                alwaysEmpty = false;
            }
            else {

                List<Contact> listContactCopy = List.copyOf(listContact);
                for (Contact c : listContactCopy) {
                    List<QuestioningAccreditation> listAccreditations = questioningAccreditationService.findByIdContact(c.getIdentifier());
                    List<Partitioning> listPart = new ArrayList<>();
                    campaignService.findbyPeriod(period).stream().forEach(camp -> listPart.addAll(camp.getPartitionings()));
                    boolean hasHabYear = false;

                    for (QuestioningAccreditation acc : listAccreditations) {
                        for (Partitioning part : listPart) {
                            if (acc.getQuestioning().getIdPartitioning().equals(part.getId())) {
                                hasHabYear = true;
                                break;
                            }
                        }
                    }
                    if ( !hasHabYear) {
                        listContact.remove(c);
                    }

                }
            }

        }
        return listContact;
    }

    private SearchContactDto transformContactDaoToDto(Contact c) {

        SearchContactDto searchContact = new SearchContactDto();
        List<String> listAccreditations = new ArrayList<>();;

        searchContact.setIdentifier(c.getIdentifier());
        searchContact.setFirstName(c.getFirstName());
        searchContact.setLastName(c.getLastName());
        searchContact.setEmail(c.getEmail());

        List<QuestioningAccreditation> accreditations = questioningAccreditationService.findByIdContact(c.getIdentifier());
        for (QuestioningAccreditation questioningAccreditation : accreditations) {
            Questioning questioning = questioningAccreditation.getQuestioning();
            Partitioning part = partitioningService.findById(questioning.getIdPartitioning());
            listAccreditations.add(partitioningService.getCampaignWording(part) + " - " + questioningAccreditation.getQuestioning().getSurveyUnit().getIdSu());
        }
        searchContact.setAccreditationsList(listAccreditations);
        return searchContact;
    }

    public List<SearchContactDto> transformListContactDaoToDto(List<Contact> listContacts) {

        List<SearchContactDto> listResult = new ArrayList<>();
        for (Contact c : listContacts) {
            listResult.add(transformContactDaoToDto(c));
        }
        return listResult;
    }

    private List<Contact> findContactByPartitioning(Partitioning part) {
        List<Questioning> listQuestioning = new ArrayList<>();
        List<Contact> listContact = new ArrayList<>();
        listQuestioning.addAll(questioningService.fingByIdPartitioning(part.getId()));
        listQuestioning.stream().forEach(q -> q.getQuestioningAccreditations().stream().forEach(acc -> {
            listContact.add(contactService.findByIdentifier(acc.getIdContact()));
        }));
        return listContact;
    }

    public List<SearchContactDto> transformListStringToDto(List<String> listIdentifiers) {

        List<SearchContactDto> listResult = new ArrayList<>();
        for (String ident : listIdentifiers) {

            listResult.add(transformContactDaoToDto(contactService.findByIdentifier(ident)));
        }
        return listResult;
    }

    /**
     * Search for the list of qualified contacts who can respond for one survey unit
     * @param idSu
     * @return List<Contact>
     */
    private List<Contact> findContactsByIdSu(String idSu) {
        List<Contact> listReturn = new ArrayList<>();
        List<String> listIdentifiers = surveyUnitService.findIdContactsByIdSu(idSu);
        for (String ident : listIdentifiers) {
            listReturn.add(contactService.findByIdentifier(ident));

        }
        return listReturn;
    }

    /**
     * Search for the list of qualified contacts who can respond for one survey unit
     * @param surveyUnitId
     * @return List<Contact>
     */
    private List<Contact> findContactsBySurveyUnitId(String surveyUnitId) {
        List<Contact> listReturn = new ArrayList<>();
        List<String> listIdentifiers = surveyUnitService.findIdContactbySurveyUnitId(surveyUnitId);
        for (String ident : listIdentifiers) {
            listReturn.add(contactService.findByIdentifier(ident));

        }
        return listReturn;
    }

    /**
     * Search for the list of qualified contacts who can respond for one survey unit
     * @param idSu
     * @return List<Contact>
     */
    private List<Contact> findContactsByCompanyName(String companyName) {
        List<Contact> listReturn = new ArrayList<>();
        List<String> listIdentifiers = surveyUnitService.findIdContactsbyCompanyName(companyName);
        for (String ident : listIdentifiers) {
            listReturn.add(contactService.findByIdentifier(ident));
        }
        return listReturn;
    }

}
