package fr.insee.survey.datacollectionmanagement.query.service;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.query.dto.MySurveyDto;

@Service
public interface MySurveysService {

    List<MySurveyDto> getListMySurveys(String id);

}
