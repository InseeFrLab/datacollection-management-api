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
        return surveyUnitRepository.findById(idSu).orElseThrow();
    }

    @Override
    public List<SurveyUnit> findbyIdentificationCode(String identificationCode) {
        return surveyUnitRepository.findAllByIdentificationCode(identificationCode);
    }

    @Override
    public List<SurveyUnit> findbyIdentificationName(String identificationName) {
        return surveyUnitRepository.findByIdentificationNameIgnoreCase(identificationName);
    }

    @Override
    public Page<SurveyUnit> findAll(Pageable pageable) {
        return surveyUnitRepository.findAll(pageable);
    }

    @Override
    public SurveyUnit saveSurveyUnit(SurveyUnit surveyUnit) {
        return surveyUnitRepository.save(surveyUnit);
    }

    @Override
    public void deleteSurveyUnit(String id) {
        surveyUnitRepository.deleteById(id);

    }
}
