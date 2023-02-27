package fr.insee.survey.datacollectionmanagement.user.service;


import fr.insee.survey.datacollectionmanagement.user.domain.SourceAccreditation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SourceAccreditationService {

    public List<SourceAccreditation> findByUserIdentifier(String id);

    public Page<SourceAccreditation> findAll(Pageable pageable);

    public Optional<SourceAccreditation> findById(Long id);

    public SourceAccreditation saveSourceAccreditation(SourceAccreditation sourceAccreditation);

    public void deleteAccreditation(SourceAccreditation c);
}