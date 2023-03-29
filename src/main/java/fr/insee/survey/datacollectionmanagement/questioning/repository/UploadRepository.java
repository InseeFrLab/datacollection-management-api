package fr.insee.survey.datacollectionmanagement.questioning.repository;

import fr.insee.survey.datacollectionmanagement.questioning.domain.Upload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface UploadRepository extends JpaRepository<Upload, Long> {

    Collection<Upload> findByQuestioningEventsIsEmpty();
}
