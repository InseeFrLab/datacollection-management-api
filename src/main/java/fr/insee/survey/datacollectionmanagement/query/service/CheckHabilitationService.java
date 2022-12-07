package fr.insee.survey.datacollectionmanagement.query.service;

import org.springframework.stereotype.Service;

@Service
public interface CheckHabilitationService {

    boolean checkHabilitation(String identifier, String idSu, String campaignId);

}
