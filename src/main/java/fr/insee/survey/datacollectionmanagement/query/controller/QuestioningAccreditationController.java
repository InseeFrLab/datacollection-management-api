package fr.insee.survey.datacollectionmanagement.query.controller;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import fr.insee.survey.datacollectionmanagement.constants.Constants;
import fr.insee.survey.datacollectionmanagement.contact.service.ContactService;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Partitioning;
import fr.insee.survey.datacollectionmanagement.metadata.service.PartitioningService;
import fr.insee.survey.datacollectionmanagement.questioning.domain.Questioning;
import fr.insee.survey.datacollectionmanagement.questioning.domain.QuestioningAccreditation;
import fr.insee.survey.datacollectionmanagement.questioning.dto.QuestioningAccreditationDto;
import fr.insee.survey.datacollectionmanagement.questioning.service.QuestioningAccreditationService;
import fr.insee.survey.datacollectionmanagement.questioning.service.QuestioningService;
import fr.insee.survey.datacollectionmanagement.view.service.ViewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin
@Tag(name = "2 - Questioning", description = "Enpoints to create, update, delete and find entities around the samples")
public class QuestioningAccreditationController {

    static final Logger LOGGER = LoggerFactory.getLogger(QuestioningAccreditationController.class);

    @Autowired
    private QuestioningAccreditationService questioningAccreditationService;

    @Autowired
    private QuestioningService questioningService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private PartitioningService partitioningService;

    @Autowired
    private ViewService viewService;

    @Autowired
    private ModelMapper modelMapper;

    @Operation(summary = "Search for questioning accreditations by questioning id")
    @GetMapping(value = Constants.API_QUESTIONINGS_ID_QUESTIONING_ACCREDITATIONS_ID, produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = QuestioningAccreditationDto.class)))),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    public ResponseEntity<?> getQuestioningAccreditation(@PathVariable("id") Long id) {

        try {
            Questioning questioning = questioningService.findbyId(id);
            questioning.getQuestioningAccreditations();
            return new ResponseEntity<>(
                    questioning.getQuestioningAccreditations().stream().map(c -> convertToDto(c))
                            .collect(Collectors.toList()),
                    HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("Questioning does not exist", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<String>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Create or update a questioning accreditation for a questioning")
    @PostMapping(value = Constants.API_QUESTIONINGS_ID_QUESTIONING_ACCREDITATIONS_ID, produces = "application/json", consumes = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",

                    content = @Content(schema = @Schema(implementation = QuestioningAccreditationDto.class))),
            @ApiResponse(responseCode = "404", description = "NotFound")
    })
    @Transactional
    public ResponseEntity<?> postQuestioningAccreditation(@PathVariable("id") Long id,
            @RequestBody QuestioningAccreditationDto questioningAccreditationDto) {

        Questioning questioning = null;

        String idContact = questioningAccreditationDto.getIdContact();

        // Check if questioning exists
        try {
            questioning = questioningService.findbyId(id);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Questioning does not exist");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }

        // Check if contact exists
        try {
            contactService.findByIdentifier(idContact);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Contact does not exist");
        }

        HttpHeaders responseHeaders = new HttpHeaders();

        // save new accreditation or update existing one
        Set<QuestioningAccreditation> setExistingAccreditations = questioning.getQuestioningAccreditations();

        List<QuestioningAccreditation> listContactAccreditations = setExistingAccreditations.stream()
                .filter(acc -> acc.getIdContact().equals(idContact)).collect(Collectors.toList());

        if (listContactAccreditations.isEmpty()) {
            // Create new accreditation
            QuestioningAccreditation questioningAccreditation = convertToEntity(questioningAccreditationDto);
            questioningAccreditation.setQuestioning(questioning);
            setExistingAccreditations.add(questioningAccreditation);
            questioningAccreditationService.saveQuestioningAccreditation(questioningAccreditation);
            questioningService.saveQuestioning(questioning);

            // create view
            Partitioning part = partitioningService.findById(questioning.getIdPartitioning());
            viewService.createView(idContact, questioning.getSurveyUnit().getIdSu(),
                    part.getCampaign().getCampaignId());

            // location header
            responseHeaders.set(HttpHeaders.LOCATION,
                    ServletUriComponentsBuilder.fromCurrentRequest().path(questioningAccreditation.getId().toString())
                            .toUriString());

            return ResponseEntity.status(HttpStatus.CREATED).headers(responseHeaders)
                    .body(convertToDto(questioningAccreditation));

        } else {
            // update accreditation
            QuestioningAccreditation questioningAccreditation = listContactAccreditations.get(0);
            questioningAccreditationDto.setId(questioningAccreditation.getId());
            questioningAccreditation = convertToEntity(questioningAccreditationDto);
            questioningAccreditation.setQuestioning(questioning);
            questioningAccreditationService.saveQuestioningAccreditation(questioningAccreditation);

            // view already exists

            // location header
            responseHeaders.set(HttpHeaders.LOCATION,
                    ServletUriComponentsBuilder.fromCurrentRequest().path(questioningAccreditation.getId().toString())
                            .toUriString());
            return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders)
                    .body(convertToDto(questioningAccreditation));
        }

    }

    private QuestioningAccreditation convertToEntity(QuestioningAccreditationDto questioningAccreditationDto) {
        return modelMapper.map(questioningAccreditationDto, QuestioningAccreditation.class);
    }

    private QuestioningAccreditationDto convertToDto(QuestioningAccreditation questioningAccreditation) {
        return modelMapper.map(questioningAccreditation, QuestioningAccreditationDto.class);
    }

}
