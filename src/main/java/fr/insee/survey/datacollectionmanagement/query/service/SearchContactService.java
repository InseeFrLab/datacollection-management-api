package fr.insee.survey.datacollectionmanagement.query.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.insee.survey.datacollectionmanagement.contact.domain.Contact;
import fr.insee.survey.datacollectionmanagement.query.dto.SearchContactDto;
import fr.insee.survey.datacollectionmanagement.view.domain.View;

public interface SearchContactService {

    /**
     * Search contact in the 3 seperated domains: contacts, questioning and metadata
     * @param identifier
     * @param lastName
     * @param firstName
     * @param email
     * @param idSu
     * @param surveyUnitId
     * @param companyName
     * @param source
     * @param year
     * @param period
     * @return
     */
    List<Contact> searchContactCrossDomain(
        String identifier,
        String lastName,
        String firstName,
        String email,
        String idSu,
        String surveyUnitId,
        String companyName,
        String source,
        String year,
        String period);
    
    
    List<View> searchContactV2CrossDomain(
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
        Pageable pageable);

    /**
     * Search for contacts in 1 domain (copy of accreditations and metadata into contacts)
     * @param identifier
     * @param lastName
     * @param firstName
     * @param email
     * @param idSu
     * @param surveyUnitId
     * @param companyName
     * @param source
     * @param year
     * @param period
     * @param pageable
     * @return
     */
    Page<Contact> searchContactV3CrossDomain(
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
        Pageable pageable);

    /**
     * Search contact in the 2 separated domains: contacts, questioning (with a copy of metadata in the questioning domain)
     * @param identifier
     * @param lastName
     * @param firstName
     * @param email
     * @param idSu
     * @param surveyUnitId
     * @param companyName
     * @param source
     * @param year
     * @param period
     * @return
     */
    List<Contact> searchContactV3bisCrossDomain(
        String identifier,
        String lastName,
        String firstName,
        String email,
        String idSu,
        String surveyUnitId,
        String companyName,
        String source,
        String year,
        String period);

    /**
     * No separated domains (SQL query to search contact)
     * @param identifier
     * @param lastName
     * @param firstName
     * @param email
     * @param idSu
     * @param surveyUnitId
     * @param companyName
     * @param source
     * @param year
     * @param period
     * @return
     */
    Page<Contact> searchContactV4CrossDomain(
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
        Pageable pageable);

    List<SearchContactDto> transformListContactDaoToDto(List<Contact> listContacts);

    List<SearchContactDto> transformListStringToDto(List<String> listIdentifiers);

    List<SearchContactDto> transformPageContactDaoToDtoV3(Page<Contact> listContacts);

    List<SearchContactDto> transformListViewDaoToDto(List<View> subList);


    List<SearchContactDto> transformPageContactDaoToDto(Page<Contact> listContacts);

}
