package fr.insee.survey.datacollectionmanagement.query.service.impl;

import fr.insee.survey.datacollectionmanagement.config.ApplicationConfig;
import fr.insee.survey.datacollectionmanagement.constants.Constants;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Campaign;
import fr.insee.survey.datacollectionmanagement.metadata.service.CampaignService;
import fr.insee.survey.datacollectionmanagement.query.dto.HabilitationDto;
import fr.insee.survey.datacollectionmanagement.query.service.CheckHabilitationService;
import fr.insee.survey.datacollectionmanagement.user.domain.User;
import fr.insee.survey.datacollectionmanagement.user.service.UserService;
import fr.insee.survey.datacollectionmanagement.view.service.ViewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class CheckHabilitationServiceImpl implements CheckHabilitationService {

    static final Logger LOGGER = LoggerFactory.getLogger(CheckHabilitationServiceImpl.class);

    @Autowired
    ApplicationConfig applicationConfig;

    @Autowired
    private ViewService viewService;

    @Autowired
    CampaignService campaignService;

    @Autowired
    UserService userService;

    @Override
    public ResponseEntity<HabilitationDto> checkHabilitation(String role, String idSu, String campaignId, HttpServletRequest request) {

        HabilitationDto resp = new HabilitationDto();

        //noauth
        if (!applicationConfig.getAuthType().equals("OIDC")) {
            resp.setHabilitated(true);
            return new ResponseEntity<>(resp, HttpStatus.OK);
        }
        String idec = request.getRemoteUser().toUpperCase();
        //admin
        if(isUserInRole(request, applicationConfig.getRoleAdmin())){
            LOGGER.info("Check habilitation of admin {} for accessing survey-unit {} of campaign {} resulted in true",idec,idSu,campaignId);
            resp.setHabilitated(true);
            return new ResponseEntity<>(resp, HttpStatus.OK);
        }

        //respondents
        if (role == null || role.isBlank() || role.equals(Constants.INTERVIEWER) )
        {
            if(isUserInRole(request, applicationConfig.getRoleRespondent())) {
                boolean habilitated = viewService.countViewByIdentifierIdSuCampaignId(idec, idSu, campaignId) != 0;
                LOGGER.info("Check habilitation of interviewer {} for accessing survey-unit {} of campaign {} resulted in {}", idec, idSu, campaignId, habilitated);
                resp.setHabilitated(habilitated);
                return new ResponseEntity<>(resp, HttpStatus.OK);
            }
            LOGGER.warn("Check habilitation of interviewer {} for accessing survey-unit {} of campaign {} - no respondent habilitation found in token - check habilitation: false", idec, idSu, campaignId);
            resp.setHabilitated(false);
            return new ResponseEntity<>(resp, HttpStatus.OK);
        }


        // internal users
        Optional<User> user=  userService.findByIdentifier(idec);
        if(role.equals(Constants.REVIEWER) ) {
            if(isUserInRole(request, applicationConfig.getRoleInternalUser())) {
                if (user.isPresent()) {
                    String userRole;
                    List<String> accreditedSources = new ArrayList<>();
                    userRole = user.get().getRole().toString();
                    if (userRole.equals(User.UserRoleType.assistance)) {
                        resp.setHabilitated(false);
                        LOGGER.warn("User '{}' has assistance profile - check habilitation: false", idec);
                        return new ResponseEntity<>(resp, HttpStatus.OK);
                    }
                    accreditedSources = userService.findAccreditedSources(user.get().getIdentifier());
                    Optional<Campaign> campaign = campaignService.findById(campaignId);
                    if (campaign.isPresent()) {
                        String sourceSearched = campaign.get().getSurvey().getSource().getId();
                        if (accreditedSources.contains(sourceSearched)) {
                            LOGGER.info("User {} with profile {} -  accreditation found for campaign {} - Check habilitation:true", idec, userRole, campaignId);
                            resp.setHabilitated(true);
                            return new ResponseEntity<>(resp, HttpStatus.OK);
                        }
                        LOGGER.warn("User {} with profile {} -  has no accreditation for campaign {} - Check habilitation:false", idec, userRole, campaignId);
                        resp.setHabilitated(false);
                        return new ResponseEntity<>(resp, HttpStatus.OK);
                    }
                    LOGGER.warn("Check habilitation of user {} for accessing survey-unit {} of campaign {} - campaign doesn't exist - check habilitation:false", idec, idSu, campaign);
                    resp.setHabilitated(false);
                    return new ResponseEntity<>(resp, HttpStatus.OK);
                }

                resp.setHabilitated(false);
                LOGGER.warn("User '{}' doesn't exists", idec);
                return new ResponseEntity<>(resp, HttpStatus.OK);
            }
            resp.setHabilitated(false);
            LOGGER.warn("User {}  -  internal user habilitation not found in token - Check habilitation:false", idec);
            return new ResponseEntity<>(resp, HttpStatus.OK);
        }

        resp.setHabilitated(false);
        LOGGER.warn("Only '{}' ans '{}' are accepted as a role in query argument", Constants.REVIEWER, Constants.INTERVIEWER);
        return new ResponseEntity<>(resp, HttpStatus.OK);

    }

    private boolean isUserInRole(HttpServletRequest request, List<String> role) {

       return role.stream().anyMatch(r -> request.isUserInRole(r));
    }

}
