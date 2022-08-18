package fr.insee.survey.datacollectionmanagement.questioning.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.questioning.domain.SurveyUnit;
import fr.insee.survey.datacollectionmanagement.questioning.repository.SurveyUnitRepository;
import fr.insee.survey.datacollectionmanagement.questioning.service.SurveyUnitService;

@Service
public class SurveyUnitServiceImpl implements SurveyUnitService {

    @Autowired
    private SurveyUnitRepository surveyUnitRepository;

    @Override
    public SurveyUnit findbyId(String idSu) {
        return surveyUnitRepository.findById(idSu).orElse(null);
    }

    @Override
    public List<SurveyUnit> findbySurveyUnitId(String surveyUnitId) {
        return surveyUnitRepository.findAllBySurveyUnitId(surveyUnitId);
    }

    @Override
    public List<SurveyUnit> findbyCompanyName(String companyName) {
        return surveyUnitRepository.findByCompanyNameIgnoreCase(companyName);
    }

    @Override
    public List<String> findIdContactbySurveyUnitId(String surveyUnitId) {
        return surveyUnitRepository.findIdContactsBySurveyUnitId(surveyUnitId);
    }

    @Override
    public List<String> findIdContactsByIdSu(String idSu) {
        return surveyUnitRepository.findIdContactsByIdSu(idSu);
    }

    @Override
    public List<String> findIdContactsbyCompanyName(String companyName) {
        return surveyUnitRepository.findIdContactsByCompanyName(companyName);

    }

    @Override
    public Page<SurveyUnit> findAll(Pageable pageable) {
        return surveyUnitRepository.findAll(pageable);
    }

    @Override
    public SurveyUnit updateSurveyUnit(SurveyUnit surveyUnit) {
        return surveyUnitRepository.save(surveyUnit);
    }

    @Override
    public void deleteSurveyUnit(String id) {
        surveyUnitRepository.deleteById(id);
        
    }
}
