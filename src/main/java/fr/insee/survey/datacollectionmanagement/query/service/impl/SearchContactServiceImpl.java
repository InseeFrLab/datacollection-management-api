package fr.insee.survey.datacollectionmanagement.query.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import fr.insee.survey.datacollectionmanagement.contact.domain.Contact;
import fr.insee.survey.datacollectionmanagement.contact.service.ContactService;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Campaign;
import fr.insee.survey.datacollectionmanagement.metadata.service.CampaignService;
import fr.insee.survey.datacollectionmanagement.query.dto.SearchContactDto;
import fr.insee.survey.datacollectionmanagement.query.service.SearchContactService;
import fr.insee.survey.datacollectionmanagement.questioning.domain.SurveyUnit;
import fr.insee.survey.datacollectionmanagement.questioning.service.SurveyUnitService;
import fr.insee.survey.datacollectionmanagement.view.domain.View;
import fr.insee.survey.datacollectionmanagement.view.service.ViewService;
import lombok.EqualsAndHashCode;

@Service
@EqualsAndHashCode
public class SearchContactServiceImpl implements SearchContactService {

    @Autowired
    private ContactService contactService;

    @Autowired
    private SurveyUnitService surveyUnitService;

    @Autowired
    private CampaignService campaignService;

    @Autowired
    @EqualsAndHashCode.Exclude
    private ViewService viewService;;

    @Override
    public List<View> searchContactCrossDomain(
            String identifier,
            String lastName,
            String firstName,
            String email,
            String idSu,
            String identificationCode,
            String identificationName,
            String source,
            String year,
            String period,
            Pageable pageable) {

        List<View> listView = new ArrayList<>();
        boolean alwaysEmpty = true;

        if (!StringUtils.isEmpty(identifier)) {
            View contactView = viewService.findFirstViewByIdentifier(identifier);
            if (contactView != null)
                listView.add(contactView);
            alwaysEmpty = false;
        }

        if (!StringUtils.isEmpty(idSu)) {
            if (listView.isEmpty() && alwaysEmpty) {
                listView = viewService.findViewByIdSu(idSu);
                alwaysEmpty = false;
            } else if (!alwaysEmpty) {
                listView = listView.stream().filter(v -> viewService.findViewByIdSu(idSu).contains(v))
                        .collect(Collectors.toList());
            }
        }

        if (!StringUtils.isEmpty(source) && !StringUtils.isEmpty(year) && !StringUtils.isEmpty(period)) {
            if (listView.isEmpty() && alwaysEmpty) {
                List<Campaign> listCampains = campaignService.findbySourceYearPeriod(source, Integer.parseInt(year),
                        period);
                for (Campaign campain : listCampains) {
                    listView.addAll(viewService.findViewByCampaignId(campain.getCampaignId()));
                }

                alwaysEmpty = false;
            } else if (!alwaysEmpty) {
                List<Campaign> listCampains = campaignService.findbySourceYearPeriod(source, Integer.parseInt(year),
                        period);
                List<View> listViewC = new ArrayList<>();
                for (Campaign c : listCampains) {
                    listViewC
                            .addAll(listView.stream()
                                    .filter(v -> viewService.findViewByCampaignId(c.getCampaignId()).contains(v))
                                    .collect(Collectors.toList()));
                }
                listView = listViewC;
            }
        }
        if ((!StringUtils.isEmpty(source) || !StringUtils.isEmpty(period)) && StringUtils.isEmpty(year)) {
            if (listView.isEmpty() && alwaysEmpty) {
                List<Campaign> listCampains = campaignService.findbySourcePeriod(source, period);
                for (Campaign campain : listCampains) {
                    listView.addAll(viewService.findViewByCampaignId(campain.getCampaignId()));
                }

                alwaysEmpty = false;
            } else if (!alwaysEmpty) {
                List<Campaign> listCampains = campaignService.findbySourcePeriod(source, period);
                List<View> listViewC = new ArrayList<>();
                for (Campaign c : listCampains) {
                    listViewC
                            .addAll(listView.stream()
                                    .filter(v -> viewService.findViewByCampaignId(c.getCampaignId()).contains(v))
                                    .collect(Collectors.toList()));
                }
                listView = listViewC;
            }
        }

        if (!StringUtils.isEmpty(lastName)) {
            if (listView.isEmpty() && alwaysEmpty) {
                List<Contact> listC = contactService.findByLastName(lastName);
                for (Contact c : listC) {
                    listView.add(viewService.findFirstViewByIdentifier(c.getIdentifier()));
                }
                alwaysEmpty = false;
            } else if (!alwaysEmpty)

                listView = listView.stream()
                        .filter(v -> lastName
                                .equalsIgnoreCase(contactService.findByIdentifier(v.getIdentifier()).getLastName()))
                        .collect(Collectors.toList());
        }

        if (!StringUtils.isEmpty(firstName)) {
            if (listView.isEmpty() && alwaysEmpty) {
                List<Contact> listC = contactService.findByFirstName(firstName);
                for (Contact c : listC) {
                    listView.add(viewService.findFirstViewByIdentifier(c.getIdentifier()));
                }
                alwaysEmpty = false;
            } else if (!alwaysEmpty)

                listView = listView.stream()
                        .filter(v -> firstName
                                .equalsIgnoreCase(contactService.findByIdentifier(v.getIdentifier()).getFirstName()))
                        .collect(Collectors.toList());
        }

        if (!StringUtils.isEmpty(email)) {
            if (listView.isEmpty() && alwaysEmpty) {
                List<Contact> listC = contactService.findByEmail(email);
                for (Contact c : listC) {
                    listView.add(viewService.findFirstViewByIdentifier(c.getIdentifier()));
                }
                alwaysEmpty = false;
            } else if (!alwaysEmpty)

                listView = listView.stream().filter(
                        v -> email.equalsIgnoreCase(contactService.findByIdentifier(v.getIdentifier()).getEmail()))
                        .collect(Collectors.toList());
        }

        if (!StringUtils.isEmpty(identificationCode)) {
            if (listView.isEmpty() && alwaysEmpty) {
                List<SurveyUnit> listSurveyUnits = surveyUnitService.findbyIdentificationCode(identificationCode);
                for (SurveyUnit s : listSurveyUnits) {
                    listView.addAll(viewService.findViewByIdSu(s.getIdSu()));
                }
                alwaysEmpty = false;
            } else if (!alwaysEmpty) {
                List<SurveyUnit> listSurveyUnits = surveyUnitService.findbyIdentificationCode(identificationCode);
                for (SurveyUnit s : listSurveyUnits) {
                    listView = listView.stream()
                            .filter(v -> identificationCode.equalsIgnoreCase(s.getIdentificationCode()))
                            .collect(Collectors.toList());
                }

            }
        }

        if (!StringUtils.isEmpty(identificationName)) {
            if (listView.isEmpty() && alwaysEmpty) {
                List<SurveyUnit> listSurveyUnits = surveyUnitService.findbyIdentificationName(identificationName);
                for (SurveyUnit s : listSurveyUnits) {
                    listView.addAll(viewService.findViewByIdSu(s.getIdSu()));
                }
                alwaysEmpty = false;
            } else if (!alwaysEmpty) {
                List<SurveyUnit> listSurveyUnits = surveyUnitService.findbyIdentificationCode(identificationCode);
                for (SurveyUnit s : listSurveyUnits) {
                    listView = listView.stream()
                            .filter(v -> identificationName.equalsIgnoreCase(s.getIdentificationName()))
                            .collect(Collectors.toList());
                }

            }
        }
        return listView;
    }

    @Override
    public List<SearchContactDto> transformListViewDaoToDto(List<View> listView) {
        List<SearchContactDto> listResult = new ArrayList<>();
        for (View v : listView) {

            SearchContactDto searchContact = new SearchContactDto();
            Contact c = contactService.findByIdentifier(v.getIdentifier());
            searchContact.setIdentifier(c.getIdentifier());
            searchContact.setFirstName(c.getFirstName());
            searchContact.setLastName(c.getLastName());
            searchContact.setEmail(c.getEmail());

            listResult.add(searchContact);
        }
        return listResult;
    }
}
