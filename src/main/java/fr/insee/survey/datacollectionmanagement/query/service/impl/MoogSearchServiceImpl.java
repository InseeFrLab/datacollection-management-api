package fr.insee.survey.datacollectionmanagement.query.service.impl;

import java.util.ArrayList;
import java.util.List;

import fr.insee.survey.datacollectionmanagement.metadata.domain.Campaign;
import fr.insee.survey.datacollectionmanagement.metadata.service.CampaignService;
import fr.insee.survey.datacollectionmanagement.query.domain.MoogCampaign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.contact.domain.Contact;
import fr.insee.survey.datacollectionmanagement.contact.service.ContactService;
import fr.insee.survey.datacollectionmanagement.query.dto.MoogSearchDto;
import fr.insee.survey.datacollectionmanagement.query.service.MoogSearchService;
import fr.insee.survey.datacollectionmanagement.view.domain.View;
import fr.insee.survey.datacollectionmanagement.view.service.ViewService;

@Service
public class MoogSearchServiceImpl implements MoogSearchService {

    @Autowired
    private ViewService viewService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private CampaignService campaignService;

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
            moogSearchDto.setAddress(c.getAddress().getZipCode().concat(c.getAddress().getCity()));
            moogSearchDto.setIdSu(view.getIdSu());
            moogSearchDto.setCampaign(moogCampaign);
            moogSearchDto.setFirstName(c.getFirstName());
            moogSearchDto.setLastName(c.getLastName());
            listResult.add(moogSearchDto);
        }
        return listResult;
    }

}
