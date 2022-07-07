package fr.insee.survey.datacollectionmanagement.view.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.view.domain.View;
import fr.insee.survey.datacollectionmanagement.view.repository.ViewRepository;
import fr.insee.survey.datacollectionmanagement.view.service.ViewService;

@Service
public class ViewServiceImpl implements ViewService {

    @Autowired
    private ViewRepository viewRepository;

    @Override
    public List<View> findViewByIdentifier(String identifier) {
        return viewRepository.findByIdentifier(identifier);
    }

    @Override
    public List<View> findViewByCampaignId(String campaignId) {
        return viewRepository.findByCampaignId(campaignId);
    }

    @Override
    public List<View> findViewByIdSu(String idSu) {
        return viewRepository.findByIdSu(idSu);
    }

}
