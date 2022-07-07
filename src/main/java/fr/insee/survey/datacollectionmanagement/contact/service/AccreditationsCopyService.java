package fr.insee.survey.datacollectionmanagement.contact.service;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.contact.domain.AccreditationsCopy;

@Service
public interface AccreditationsCopyService {

    List<AccreditationsCopy> findAddredationsCopyOfContact(String idContact);

}
