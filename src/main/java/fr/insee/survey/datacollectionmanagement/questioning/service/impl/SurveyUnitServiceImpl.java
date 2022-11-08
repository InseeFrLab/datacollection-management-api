package fr.insee.survey.datacollectionmanagement.questioning.service.impl;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.questioning.domain.SurveyUnit;
import fr.insee.survey.datacollectionmanagement.questioning.repository.SurveyUnitAddressRepository;
import fr.insee.survey.datacollectionmanagement.questioning.repository.SurveyUnitRepository;
import fr.insee.survey.datacollectionmanagement.questioning.service.SurveyUnitService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SurveyUnitServiceImpl implements SurveyUnitService {

    @Autowired
    private SurveyUnitRepository surveyUnitRepository;

    @Autowired
    private SurveyUnitAddressRepository surveyUnitAddressRepository;

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
    public SurveyUnit saveSurveyUnitAndAddress(SurveyUnit surveyUnit) {

        if (surveyUnit.getSurveyUnitAddress() != null) {
            try {
                SurveyUnit existingSurveyUnit = findbyId(surveyUnit.getIdSu());
                if (surveyUnit.getSurveyUnitAddress() != null) {
                    if (existingSurveyUnit.getSurveyUnitAddress() != null) {
                        surveyUnit.getSurveyUnitAddress().setId(existingSurveyUnit.getSurveyUnitAddress().getId());
                    }
                }
            } catch (NoSuchElementException e) {
                log.debug("Survey unit does not exist");
            }
            surveyUnitAddressRepository.save(surveyUnit.getSurveyUnitAddress());
        }

        return surveyUnitRepository.save(surveyUnit);

    }

    @Override
    public void deleteSurveyUnit(String id) {
        surveyUnitRepository.deleteById(id);

    }

}
