package fr.insee.survey.datacollectionmanagement.metadata.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.metadata.domain.Campaign;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Survey;
import fr.insee.survey.datacollectionmanagement.metadata.repository.SurveyRepository;
import fr.insee.survey.datacollectionmanagement.metadata.service.SurveyService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SurveyServiceImpl implements SurveyService {

    @Autowired
    private SurveyRepository surveyRepository;

    @Override
    public List<Survey> findByYear(int year) {
        return surveyRepository.findByYear(year);
    }

    @Override
    public Optional<Survey> findById(String id) {
        return surveyRepository.findById(id);
    }

    @Override
    public Page<Survey> findAll(Pageable pageable) {
        return surveyRepository.findAll(pageable);
    }

    @Override
    public Survey insertOrUpdateSurvey(Survey survey) {
        Optional<Survey> surveyBase = findById(survey.getId());
        if (!surveyBase.isPresent()) {
            log.info("Create survey with the id {}", survey.getId());
            return surveyRepository.save(survey);
        }
        log.info("Update survey with the id {}", survey.getId());
        survey.setCampaigns(surveyBase.get().getCampaigns());
        return surveyRepository.save(survey);
    }

    @Override
    public void deleteSurveyById(String id) {
        surveyRepository.deleteById(id);
    }

    @Override
    public Survey addCampaignToSurvey(Survey survey, Campaign campaign) {

        Optional<Survey> surveyBase = findById(survey.getId());
        if (surveyBase.isPresent() && isCampaignPresent(campaign, surveyBase.get())) {
            survey.setCampaigns(surveyBase.get().getCampaigns());

        } else {

            Set<Campaign> campaigns = (!surveyBase.isPresent()) ? new HashSet<>()
                    : surveyBase.get().getCampaigns();
            campaigns.add(campaign);
            survey.setCampaigns(campaigns);
        }
        return survey;
    }
    

    private boolean isCampaignPresent(Campaign c, Survey s) {
        for (Campaign camp : s.getCampaigns()) {
            if (camp.getId().equals(c.getId())) {
                return true;
            }
        }
        return false;
    }

}
