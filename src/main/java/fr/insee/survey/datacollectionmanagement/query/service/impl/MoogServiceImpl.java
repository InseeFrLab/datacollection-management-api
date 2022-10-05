package fr.insee.survey.datacollectionmanagement.query.service.impl;

import java.util.ArrayList;
import java.util.List;

import fr.insee.survey.datacollectionmanagement.metadata.domain.Campaign;
import fr.insee.survey.datacollectionmanagement.metadata.service.CampaignService;
import fr.insee.survey.datacollectionmanagement.query.domain.MoogCampaign;
import fr.insee.survey.datacollectionmanagement.query.dto.MoogQuestioningEventDto;
import fr.insee.survey.datacollectionmanagement.query.repository.MoogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.contact.domain.Contact;
import fr.insee.survey.datacollectionmanagement.contact.service.ContactService;
import fr.insee.survey.datacollectionmanagement.query.dto.MoogSearchDto;
import fr.insee.survey.datacollectionmanagement.query.service.MoogService;
import fr.insee.survey.datacollectionmanagement.view.domain.View;
import fr.insee.survey.datacollectionmanagement.view.service.ViewService;

@Service
public class MoogServiceImpl implements MoogService {

    @Autowired
    private ViewService viewService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private CampaignService campaignService;

    @Autowired
    private MoogRepository moogRepository;

    @Override
    public List<View> moogSearch(String field) {

        List<View> listView = new ArrayList<>();
        listView.addAll(viewService.findByIdentifierContainingAndIdSuNotNull(field));
        listView.addAll(viewService.findViewByIdSuContaining(field));
        return listView;
    }

    public List<MoogSearchDto> transformListViewToListMoogSearchDto(List<View> listView) {
        List<MoogSearchDto> listResult = new ArrayList<>();
        for (View view : listView) {
            MoogSearchDto moogSearchDto = new MoogSearchDto();
            Contact c = contactService.findByIdentifier(view.getIdentifier());
            Campaign camp = campaignService.findById(view.getCampaignId());
            MoogCampaign moogCampaign = new MoogCampaign();
            moogCampaign.setId(view.getCampaignId());
            moogCampaign.setLabel(camp.getCampaignWording());
            moogCampaign.setCollectionEndDate(camp.getPartitionings().iterator().next().getClosingDate().getTime());
            moogCampaign.setCollectionStartDate(camp.getPartitionings().iterator().next().getOpeningDate().getTime());
            moogSearchDto.setIdContact(view.getIdentifier());
            moogSearchDto.setAddress(c.getAddress().getZipCode().concat(" ").concat(c.getAddress().getCity()));
            moogSearchDto.setIdSu(view.getIdSu());
            moogSearchDto.setCampaign(moogCampaign);
            moogSearchDto.setFirstName(c.getFirstName());
            moogSearchDto.setLastname(c.getLastName());
            listResult.add(moogSearchDto);
        }
        return listResult;
    }

    @Override
    public List<MoogQuestioningEventDto> getMoogEvents(String campaign, String idSu){

        List<MoogQuestioningEventDto> moogEvents = moogRepository.getEventsByIdSuByCampaign(campaign, idSu);

        Campaign camp = campaignService.findById(campaign);
        MoogCampaign moogCampaign = new MoogCampaign();
        moogCampaign.setId(campaign);
        moogCampaign.setLabel(camp.getCampaignWording());
        moogCampaign.setCollectionEndDate(camp.getPartitionings().iterator().next().getClosingDate().getTime());
        moogCampaign.setCollectionStartDate(camp.getPartitionings().iterator().next().getOpeningDate().getTime());
        MoogSearchDto surveyUnit = new MoogSearchDto();
        surveyUnit.setCampaign(moogCampaign);
        moogEvents.stream().forEach(e -> e.setSurveyUnit(surveyUnit));



        return moogEvents;
    }

}