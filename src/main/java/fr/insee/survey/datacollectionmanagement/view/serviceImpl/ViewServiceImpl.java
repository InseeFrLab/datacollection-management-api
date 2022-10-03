package fr.insee.survey.datacollectionmanagement.view.serviceImpl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
    public View findFirstViewByIdentifier(String identifier) {
        return viewRepository.findFirstByIdentifier(identifier);
    }


    @Override
    public List<View> findByIdentifierContainingAndIdSuNotNull(String identifier) {
        return viewRepository.findByIdentifierContainingAndIdSuNotNull(identifier);
    }

    @Override
    public List<View> findViewByCampaignId(String campaignId) {
        return viewRepository.findByCampaignId(campaignId);
    }

    @Override
    public List<View> findViewByIdSu(String idSu) {
        return viewRepository.findByIdSu(idSu);
    }

    @Override
    public List<View> findViewByIdSuContaining(String field) {
        return viewRepository.findByIdSuContaining(field);
    }

    @Override
    public List<View> findViewByIdentifierIdSuCampaignId(String identifier, String idSu, String campaignId) {
        return viewRepository.findViewByIdentifierAndIdSuAndCampaignId(identifier, idSu, campaignId);
    }

    @Override
    public View saveView(View view) {
        return viewRepository.save(view);
    }

    @Override
    public void deleteView(View view) {
        viewRepository.delete(view);
    }
    
    @Override
    public void deleteViewByIdentifier(String identifier) {
        viewRepository.deleteByIdentifier(identifier);
    }

    @Override
    public View createView(String identifier, String idSu, String campaignId) {
        View view = new View();
        view.setIdentifier(identifier);
        view.setCampaignId(campaignId);
        view.setIdSu(idSu);
        View contactView = findFirstViewByIdentifier(identifier);
        if (contactView !=null) {
            deleteView(contactView);
        }
        return saveView(view);
    }


}
