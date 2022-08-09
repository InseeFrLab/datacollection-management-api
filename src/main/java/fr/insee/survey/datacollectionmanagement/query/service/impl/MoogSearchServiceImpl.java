package fr.insee.survey.datacollectionmanagement.query.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.contact.domain.AccreditationsCopy;
import fr.insee.survey.datacollectionmanagement.contact.domain.Contact;
import fr.insee.survey.datacollectionmanagement.contact.service.AccreditationsCopyService;
import fr.insee.survey.datacollectionmanagement.contact.service.ContactService;
import fr.insee.survey.datacollectionmanagement.metadata.service.CampaignService;
import fr.insee.survey.datacollectionmanagement.query.dto.MoogSearchDto;
import fr.insee.survey.datacollectionmanagement.query.service.MoogSearchService;
import fr.insee.survey.datacollectionmanagement.view.domain.View;
import fr.insee.survey.datacollectionmanagement.view.service.ViewService;

@Service
public class MoogSearchServiceImpl implements MoogSearchService {

    @Autowired
    private ViewService viewService;

    @Autowired
    private AccreditationsCopyService accreditationsCopyService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private CampaignService campaignService;

    @Override
    public List<View> moogSearchV2(String field) {

        List<View> listView = new ArrayList<>();
        listView.addAll(viewService.findByIdentifierContainingAndIdSuNotNull(field));
        listView.addAll(viewService.findViewByIdSuContaining(field));
        return listView;
    }

    @Override
    public List<AccreditationsCopy> moogSearchV3(String field) {
        List<AccreditationsCopy> listResult = new ArrayList<>();
        listResult.addAll(accreditationsCopyService.findByContactIdentifierContainingOrIdSuContaining(field, field));
        return listResult;
    }

    public List<MoogSearchDto> transformListViewToListMoogSearchDto(List<View> listView) {
        List<MoogSearchDto> listResult = new ArrayList<>();
        for (View view : listView) {
            MoogSearchDto moogSearchDto = new MoogSearchDto();
            Contact c = contactService.findByIdentifier(view.getIdentifier());
            moogSearchDto.setIdentifier(view.getIdentifier());
            moogSearchDto.setAddress(c.getAddress());
            moogSearchDto.setIdSu(view.getIdSu());
            moogSearchDto.setCampaign(view.getCampaignId());
            moogSearchDto.setFirstName(c.getFirstName());
            moogSearchDto.setLastName(c.getLastName());
            listResult.add(moogSearchDto);
        }
        return listResult;
    }

    @Override
    public List<MoogSearchDto> transformListAccreditationsCopyToListMoogSearchDto(List<AccreditationsCopy> listAccreditationsCopy) {
        List<MoogSearchDto> listResult = new ArrayList<>();
        for (AccreditationsCopy acc : listAccreditationsCopy) {
            MoogSearchDto moogSearchDto = new MoogSearchDto();
            Contact c = acc.getContact();
            moogSearchDto.setIdentifier(c.getIdentifier());
            moogSearchDto.setAddress(c.getAddress());
            moogSearchDto.setIdSu(acc.getIdSu());
            moogSearchDto.setCampaign(campaignService.findbySourceYearPeriod(acc.getSourceId(), acc.getYear(), acc.getPeriod()).get(0).getCampaignId());
            moogSearchDto.setFirstName(c.getFirstName());
            moogSearchDto.setLastName(c.getLastName());
            listResult.add(moogSearchDto);
        }
        return listResult;
    }

}
