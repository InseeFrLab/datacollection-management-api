package fr.insee.survey.datacollectionmanagement.query.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import fr.insee.survey.datacollectionmanagement.config.JSONCollectionWrapper;
import fr.insee.survey.datacollectionmanagement.query.dto.MoogExtractionRowDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.contact.domain.Contact;
import fr.insee.survey.datacollectionmanagement.contact.service.ContactService;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Campaign;
import fr.insee.survey.datacollectionmanagement.metadata.service.CampaignService;
import fr.insee.survey.datacollectionmanagement.query.domain.MoogCampaign;
import fr.insee.survey.datacollectionmanagement.query.dto.MoogQuestioningEventDto;
import fr.insee.survey.datacollectionmanagement.query.dto.MoogSearchDto;
import fr.insee.survey.datacollectionmanagement.query.repository.MoogRepository;
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
            Optional<Contact> c = contactService.findByIdentifier(view.getIdentifier());
            Optional<Campaign> camp = campaignService.findById(view.getCampaignId());
            if (!camp.isPresent()) {
                throw new NoSuchElementException("campaign does not exist");
            }
            if (!c.isPresent()) {
                throw new NoSuchElementException("contact does not exist");
            }
            MoogCampaign moogCampaign = new MoogCampaign();
            moogCampaign.setId(view.getCampaignId());
            moogCampaign.setLabel(camp.get().getCampaignWording());
            moogCampaign
                    .setCollectionEndDate(camp.get().getPartitionings().iterator().next().getClosingDate().getTime());
            moogCampaign
                    .setCollectionStartDate(camp.get().getPartitionings().iterator().next().getOpeningDate().getTime());
            moogSearchDto.setIdContact(view.getIdentifier());
            moogSearchDto.setAddress(c.get().getAddress().getZipCode().concat(" ").concat(c.get().getAddress().getCityName()));
            moogSearchDto.setIdSu(view.getIdSu());
            moogSearchDto.setCampaign(moogCampaign);
            moogSearchDto.setFirstName(c.get().getFirstName());
            moogSearchDto.setLastname(c.get().getLastName());
            listResult.add(moogSearchDto);
        }
        return listResult;
    }

    @Override
    public List<MoogQuestioningEventDto> getMoogEvents(String campaign, String idSu) {

        List<MoogQuestioningEventDto> moogEvents = moogRepository.getEventsByIdSuByCampaign(campaign, idSu);

        Optional<Campaign> camp = campaignService.findById(campaign);
        if (!camp.isPresent()) {
            throw new NoSuchElementException("campaign does not exist");
        }
        MoogCampaign moogCampaign = new MoogCampaign();
        moogCampaign.setId(campaign);
        moogCampaign.setLabel(camp.get().getCampaignWording());
        moogCampaign.setCollectionEndDate(camp.get().getPartitionings().iterator().next().getClosingDate().getTime());
        moogCampaign.setCollectionStartDate(camp.get().getPartitionings().iterator().next().getOpeningDate().getTime());
        MoogSearchDto surveyUnit = new MoogSearchDto();
        surveyUnit.setCampaign(moogCampaign);
        moogEvents.stream().forEach(e -> e.setSurveyUnit(surveyUnit));

        return moogEvents;
    }

    public JSONCollectionWrapper<MoogExtractionRowDto> getExtraction(String idCampaign) {

        return new JSONCollectionWrapper<>(moogRepository.getExtraction(idCampaign));
    }

    public Collection<MoogExtractionRowDto> getSurveyUnitsToFollowUp(String idCampaign) {
        return moogRepository.getSurveyUnitToFollowUp(idCampaign);
    }

}
