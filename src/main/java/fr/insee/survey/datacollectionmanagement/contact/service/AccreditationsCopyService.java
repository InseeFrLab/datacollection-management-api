package fr.insee.survey.datacollectionmanagement.contact.service;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.contact.domain.AccreditationsCopy;

@Service
public interface AccreditationsCopyService {

    List<AccreditationsCopy> findAccreditationCopyOfContact(String identifier);
    
    List<AccreditationsCopy> findAccreditationCopy(String identifier, String idSu, String source, int year, String period);

    List<AccreditationsCopy> findByContactIdentifierContainingOrIdSuContaining(String identifier, String idSu);

}
