package fr.insee.survey.datacollectionmanagement.query.service;

import org.springframework.stereotype.Service;

@Service
public interface CheckAccreditationService {

    boolean checkAccreditation(String identifier, String idSu, String campaignId);

}
