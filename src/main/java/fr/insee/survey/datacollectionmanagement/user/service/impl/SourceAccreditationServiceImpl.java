package fr.insee.survey.datacollectionmanagement.user.service.impl;

import fr.insee.survey.datacollectionmanagement.user.domain.SourceAccreditation;
import fr.insee.survey.datacollectionmanagement.user.repository.SourceAccreditationRepository;
import fr.insee.survey.datacollectionmanagement.user.service.SourceAccreditationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SourceAccreditationServiceImpl implements SourceAccreditationService {

    @Autowired
    private SourceAccreditationRepository sourceAccreditationRepository;

    public List<SourceAccreditation> findByUserIdentifier(String id) {
        return sourceAccreditationRepository.findByIdUser(id);
    }

    @Override
    public Page<SourceAccreditation> findAll(Pageable pageable) {
        return sourceAccreditationRepository.findAll(pageable);
    }

    @Override
    public Optional<SourceAccreditation> findById(Long id) {
        return sourceAccreditationRepository.findById(id);
    }

    @Override
    public SourceAccreditation saveSourceAccreditation(SourceAccreditation sourceAccreditation) {
        return sourceAccreditationRepository.save(sourceAccreditation);
    }

    @Override
    public void deleteAccreditation(SourceAccreditation acc) {
        sourceAccreditationRepository.deleteById(acc.getId());
    }
}
