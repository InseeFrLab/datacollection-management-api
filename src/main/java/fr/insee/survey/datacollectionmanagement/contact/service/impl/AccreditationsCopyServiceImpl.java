package fr.insee.survey.datacollectionmanagement.contact.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.contact.domain.AccreditationsCopy;
import fr.insee.survey.datacollectionmanagement.contact.repository.AccreditationsCopyRepository;
import fr.insee.survey.datacollectionmanagement.contact.service.AccreditationsCopyService;

@Service
public class AccreditationsCopyServiceImpl implements AccreditationsCopyService {

    @Autowired
    private AccreditationsCopyRepository accreditationsCopyRepository;

    @Override
    public List<AccreditationsCopy> findAddredationsCopyOfContact(String identifier) {
        return accreditationsCopyRepository.findByContactIdentifier(identifier);
    }

}
