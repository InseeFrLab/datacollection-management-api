package fr.insee.survey.datacollectionmanagement.query.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.contact.domain.Contact;
import fr.insee.survey.datacollectionmanagement.contact.service.ContactService;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Partitioning;
import fr.insee.survey.datacollectionmanagement.metadata.service.PartitioningService;
import fr.insee.survey.datacollectionmanagement.query.dto.AccreditationDetail;
import fr.insee.survey.datacollectionmanagement.query.dto.SearchContactDto;
import fr.insee.survey.datacollectionmanagement.query.service.SearchContactService;
import fr.insee.survey.datacollectionmanagement.questioning.domain.Questioning;
import fr.insee.survey.datacollectionmanagement.questioning.domain.QuestioningAccreditation;
import fr.insee.survey.datacollectionmanagement.questioning.service.QuestioningAccreditationService;
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
    private PartitioningService partitioningService;

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
                List<String> listPartitioningIds = partitioningService.findIdPartitioningsBySourceIdYearPeriod(source, year, period);
                for (String partId : listPartitioningIds)
                    listContact.addAll(findContactByPartitioningId(partId));
                alwaysEmpty = false;
            }
            else {
                List<Contact> listContactCopy = List.copyOf(listContact);
                List<String> listPartitioningIds = partitioningService.findIdPartitioningsBySourceIdYearPeriod(source, year, period);
                for (Contact c : listContactCopy) {
                    List<String> listPartitioningIdContact = questioningAccreditationService.findIdPartitioningsByContactAccreditations(c.getIdentifier());
                    boolean noElementsInCommon = true;
                    if ( !listPartitioningIdContact.isEmpty()) noElementsInCommon = Collections.disjoint(listPartitioningIdContact, listPartitioningIds);
                    if (noElementsInCommon) {
                        listContact.remove(c);
                    }

                }

            }
        }

        if ( !StringUtils.isEmpty(source) && !kCampaign) {
            if (listContact.isEmpty() && alwaysEmpty) {
                List<String> listPartitioningIds = partitioningService.findIdPartitioningsBySourceId(source);
                for (String partId : listPartitioningIds)
                    listContact.addAll(findContactByPartitioningId(partId));
                alwaysEmpty = false;
            }

            else {
                List<Contact> listContactCopy = List.copyOf(listContact);
                List<String> listPartitioningIds = partitioningService.findIdPartitioningsBySourceId(source);
                for (Contact c : listContactCopy) {
                    List<String> listPartitioningIdContact = questioningAccreditationService.findIdPartitioningsByContactAccreditations(c.getIdentifier());
                    boolean noElementsInCommon = true;
                    if ( !listPartitioningIdContact.isEmpty()) noElementsInCommon = Collections.disjoint(listPartitioningIdContact, listPartitioningIds);
                    if (noElementsInCommon) {
                        listContact.remove(c);
                    }
                }
            }
        }

        if ( !StringUtils.isEmpty(year) && !kCampaign) {
            if (listContact.isEmpty() && alwaysEmpty) {
                List<String> listPartitioningIds = partitioningService.findIdPartitioningsByYear(year);
                for (String partId : listPartitioningIds)
                    listContact.addAll(findContactByPartitioningId(partId));
                alwaysEmpty = false;
            }

            else {
                List<Contact> listContactCopy = List.copyOf(listContact);
                List<String> listPartitioningIds = partitioningService.findIdPartitioningsByYear(year);
                for (Contact c : listContactCopy) {
                    List<String> listPartitioningIdContact = questioningAccreditationService.findIdPartitioningsByContactAccreditations(c.getIdentifier());
                    boolean noElementsInCommon = true;
                    if ( !listPartitioningIdContact.isEmpty()) noElementsInCommon = Collections.disjoint(listPartitioningIdContact, listPartitioningIds);
                    if (noElementsInCommon) {
                        listContact.remove(c);
                    }
                }
            }
        }

        if ( !StringUtils.isEmpty(period) && !kCampaign) {
            if (listContact.isEmpty() && alwaysEmpty) {
                List<String> listPartitioningIds = partitioningService.findIdPartitioningsByPeriod(period);
                for (String partId : listPartitioningIds)
                    listContact.addAll(findContactByPartitioningId(partId));
                alwaysEmpty = false;
            }

            else {
                List<Contact> listContactCopy = List.copyOf(listContact);
                List<String> listPartitioningIds = partitioningService.findIdPartitioningsByPeriod(period);
                for (Contact c : listContactCopy) {
                    List<String> listPartitioningIdContact = questioningAccreditationService.findIdPartitioningsByContactAccreditations(c.getIdentifier());
                    boolean noElementsInCommon = true;
                    if ( !listPartitioningIdContact.isEmpty()) noElementsInCommon = Collections.disjoint(listPartitioningIdContact, listPartitioningIds);
                    if (noElementsInCommon) {
                        listContact.remove(c);
                    }
                }

            }

        }
        return listContact;
    }

    @Override
    public List<Contact> searchContactV2CrossDomain(
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
                listContact.addAll(findContactsBySourceYearPeriod(source, year, period));
                alwaysEmpty = false;
            }
            else {
                listContact = listContact.stream().filter(c -> findContactsBySourceYearPeriod(source, year, period).contains(c)).collect(Collectors.toList());

            }
        }

        if ( !StringUtils.isEmpty(source) && !kCampaign) {
            if (listContact.isEmpty() && alwaysEmpty) {
                listContact.addAll(findContactsBySourceId(source));
                alwaysEmpty = false;
            }
            else {
                listContact = listContact.stream().filter(c -> findContactsBySourceId(source).contains(c)).collect(Collectors.toList());
            }
        }

        if ( !StringUtils.isEmpty(year) && !kCampaign) {
            if (listContact.isEmpty() && alwaysEmpty) {
                listContact.addAll(findContactsByYear(year));
                alwaysEmpty = false;
            }
            else {
                listContact = listContact.stream().filter(c -> findContactsByYear(year).contains(c)).collect(Collectors.toList());

            }
        }

        if ( !StringUtils.isEmpty(period) && !kCampaign) {
            if (listContact.isEmpty() && alwaysEmpty) {
                listContact.addAll(findContactsByPeriod(period));
                alwaysEmpty = false;
            }
            else {
                listContact = listContact.stream().filter(c -> findContactsByPeriod(period).contains(c)).collect(Collectors.toList());

            }
        }

        return listContact;
    }

    @Override
    public Page<Contact> searchContactV3CrossDomain(
        String identifier,
        String lastName,
        String firstName,
        String email,
        String idSu,
        String surveyUnitId,
        String companyName,
        String source,
        String year,
        String period,
        Pageable pageable) {
        return contactService.searchListContactAccreditationsCopy(identifier, lastName, firstName, email, idSu, surveyUnitId, companyName, source, year, period,
            pageable);
    }

    @Override
    public Page<Contact> searchContactV4CrossDomain(
        String identifier,
        String lastName,
        String firstName,
        String email,
        String idSu,
        String surveyUnitId,
        String companyName,
        String source,
        String year,
        String period,
        Pageable pageable) {
        return contactService.searchListContactSql(identifier, lastName, firstName, email, idSu, surveyUnitId, companyName, source, year, period, pageable);
    }

    private SearchContactDto transformContactDaoToDto(Contact c) {

        SearchContactDto searchContact = new SearchContactDto();
        List<AccreditationDetail> listAccreditations = new ArrayList<>();;

        searchContact.setIdentifier(c.getIdentifier());
        searchContact.setFirstName(c.getFirstName());
        searchContact.setLastName(c.getLastName());
        searchContact.setEmail(c.getEmail());

        List<QuestioningAccreditation> accreditations = questioningAccreditationService.findByIdContact(c.getIdentifier());
        for (QuestioningAccreditation questioningAccreditation : accreditations) {
            Questioning questioning = questioningAccreditation.getQuestioning();
            Partitioning part = partitioningService.findById(questioning.getIdPartitioning());

            listAccreditations.add(new AccreditationDetail(part.getCampaign().getSurvey().getSource().getIdSource(), part.getCampaign().getSurvey().getYear(),
                part.getCampaign().getPeriod(), questioningAccreditation.getQuestioning().getSurveyUnit().getIdSu()));

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

    @Override
    public List<SearchContactDto> transformPageContactDaoToDto(Page<Contact> listContacts) {
        List<SearchContactDto> listResult = new ArrayList<>();
        for (Contact c : listContacts) {
            listResult.add(transformContactDaoToDto(c));
        }
        return listResult;
    }

    private List<Contact> findContactByPartitioningId(String partitiongId) {
        List<String> listIdentifiers = questioningAccreditationService.findIdContactsByPartitionigAccredications(partitiongId);
        List<Contact> listContact = new ArrayList<>();
        listIdentifiers.stream().forEach(id -> listContact.add(contactService.findByIdentifier(id)));
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

    /**
     * Search for the list of qualified contacts who can respond for one source
     * @param idSu
     * @return List<Contact>
     */
    private List<Contact> findContactsBySourceId(String sourceId) {
        List<Contact> listReturn = new ArrayList<>();
        List<String> listIdentifiers = questioningAccreditationService.findIdContactsByIdSource(sourceId);
        for (String ident : listIdentifiers) {
            listReturn.add(contactService.findByIdentifier(ident));
        }
        return listReturn;
    }

    /**
     * Search for the list of qualified contacts who can respond for one year
     * @param idSu
     * @return List<Contact>
     */
    private List<Contact> findContactsByYear(String year) {
        List<Contact> listReturn = new ArrayList<>();
        List<String> listIdentifiers = questioningAccreditationService.findIdContactsByYear(Integer.parseInt(year));
        for (String ident : listIdentifiers) {
            listReturn.add(contactService.findByIdentifier(ident));
        }
        return listReturn;
    }

    /**
     * Search for the list of qualified contacts who can respond for one period
     * @param idSu
     * @return List<Contact>
     */
    private List<Contact> findContactsByPeriod(String period) {
        List<Contact> listReturn = new ArrayList<>();
        List<String> listIdentifiers = questioningAccreditationService.findIdContactsByPeriod(period);
        for (String ident : listIdentifiers) {
            listReturn.add(contactService.findByIdentifier(ident));
        }
        return listReturn;
    }

    /**
     * Search for the list of qualified contacts who can respond for one source
     * @param idSu
     * @return List<Contact>
     */
    private List<Contact> findContactsBySourceYearPeriod(String source, String year, String period) {
        List<Contact> listReturn = new ArrayList<>();
        List<String> listIdentifiers = questioningAccreditationService.findIdContactsBySourceYearPeriod(source, Integer.parseInt(year), period);
        for (String ident : listIdentifiers) {
            listReturn.add(contactService.findByIdentifier(ident));
        }
        return listReturn;
    }

}
