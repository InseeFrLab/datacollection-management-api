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
    public List<AccreditationsCopy> findAccreditationCopyOfContact(String identifier) {
        return accreditationsCopyRepository.findByContactIdentifier(identifier);
    }

    @Override
    public List<AccreditationsCopy> findAccreditationCopy(String identifier, String idSu, String source, int year, String period) {
        return accreditationsCopyRepository.findByContactIdentifierAndIdSuAndSourceIdAndYearAndPeriod(identifier, idSu, source, year, period);
    }

}
