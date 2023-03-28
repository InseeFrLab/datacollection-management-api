package fr.insee.survey.datacollectionmanagement.query.repository;

import fr.insee.survey.datacollectionmanagement.query.domain.Upload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface UploadRepository extends JpaRepository<Upload, Long> {

    Collection<Upload> findByQuestioningEventsIsEmpty();
}
