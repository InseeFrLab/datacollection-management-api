package fr.insee.survey.datacollectionmanagement.questioning.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.insee.survey.datacollectionmanagement.questioning.domain.Questioning;

public interface QuestioningService {

    public Page<Questioning> findAll(Pageable pageable);

    public Questioning findbyId(Long id);

    public Questioning saveQuestioning(Questioning questioning);

}
