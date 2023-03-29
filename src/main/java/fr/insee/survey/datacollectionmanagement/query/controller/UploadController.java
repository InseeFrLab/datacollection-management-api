package fr.insee.survey.datacollectionmanagement.query.controller;

import fr.insee.survey.datacollectionmanagement.config.JSONCollectionWrapper;
import fr.insee.survey.datacollectionmanagement.constants.Constants;
import fr.insee.survey.datacollectionmanagement.exception.RessourceNotValidatedException;
import fr.insee.survey.datacollectionmanagement.query.domain.ResultUpload;
import fr.insee.survey.datacollectionmanagement.query.domain.Upload;
import fr.insee.survey.datacollectionmanagement.query.dto.UploadDto;
import fr.insee.survey.datacollectionmanagement.query.service.UploadService;
import fr.insee.survey.datacollectionmanagement.questioning.domain.Questioning;
import fr.insee.survey.datacollectionmanagement.questioning.service.QuestioningEventService;
import fr.insee.survey.datacollectionmanagement.questioning.service.QuestioningService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@PreAuthorize("@AuthorizeMethodDecider.isInternalUser() "
        + "|| @AuthorizeMethodDecider.isWebClient() "
        + "|| @AuthorizeMethodDecider.isAdmin() ")
@Tag(name = "5 - Moog", description = "Enpoints for moog")
public class UploadController {
    static final Logger LOGGER = LoggerFactory.getLogger(UploadController.class);

    @Autowired
    UploadService moogUploadService;

    @Autowired
    QuestioningEventService questioningEventService;

    @Autowired
    QuestioningService questioningService;

    @DeleteMapping(value = Constants.MOOG_API_UPLOADS_ID)
    public ResponseEntity<?> deleteOneUpload(@PathVariable Long id) {
        LOGGER.info("Request DELETE for upload n° {}", id);

        Optional<Upload> upOpt = moogUploadService.findById(id);
        if(!upOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Upload does not exist");
        }
        Upload up = upOpt.get();
        up.getQuestioningEvents().stream().forEach(q -> {
            Questioning quesitoning = q.getQuestioning();
            quesitoning.setQuestioningEvents(quesitoning.getQuestioningEvents().stream()
                    .filter(qe -> !qe.equals(q)).collect(Collectors.toSet()));
            questioningService.saveQuestioning(quesitoning);
            questioningEventService.deleteQuestioningEvent(q.getId());
        });
        moogUploadService.delete(up);

        return new ResponseEntity<Upload>(HttpStatus.NO_CONTENT);


    }

    @GetMapping(value = Constants.MOOG_API_CAMPAIGN_UPLOADS, produces = "application/json")
    public JSONCollectionWrapper<Upload> displayAllUploads(@PathVariable String idCampaign) {
        LOGGER.info("Request GET for uploads");
        return new JSONCollectionWrapper<Upload>(moogUploadService.findAllByIdCampaign(idCampaign));
    }

    @PostMapping(value = Constants.MOOG_API_CAMPAIGN_UPLOADS, produces = "application/json")
    public ResultUpload addQuestioningEventViaUpload(@PathVariable String idCampaign,
                                                             @RequestBody UploadDto request) throws RessourceNotValidatedException {
        LOGGER.info("Request POST to add an upload");
        ResultUpload retourInfo = moogUploadService.save(idCampaign, request);
        return retourInfo;
    }

}
