package fr.insee.survey.datacollectionmanagement.user.repository;

import fr.insee.survey.datacollectionmanagement.user.domain.SourceAccreditation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SourceAccreditationRepository extends JpaRepository<SourceAccreditation, Long> {

    public List<SourceAccreditation> findByIdUser(String idUser);
}
