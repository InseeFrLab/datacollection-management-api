package fr.insee.survey.datacollectionmanagement.query.service;

import org.springframework.stereotype.Service;

@Service
public interface CheckAccreditationService {

    boolean checkAccreditationV2(String identifier, String idSu, String campaignId);

    boolean checkAccreditationV3(String identifier, String idSu, String campaignId);

}
