package fr.insee.survey.datacollectionmanagement.contact.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.insee.survey.datacollectionmanagement.contact.domain.AccreditationsCopy;

public interface AccreditationsCopyRepository extends JpaRepository<AccreditationsCopy, Long> {

    List<AccreditationsCopy> findByContactIdentifier(String identifier);

    List<AccreditationsCopy> findByContactIdentifierAndIdSuAndSourceIdAndYearAndPeriod(
        String identifier,
        String idSu,
        String sourceId,
        int year,
        String period);
}
