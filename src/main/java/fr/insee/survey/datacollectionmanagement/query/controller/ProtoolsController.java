package fr.insee.survey.datacollectionmanagement.query.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.insee.survey.datacollectionmanagement.constants.Constants;
import fr.insee.survey.datacollectionmanagement.contact.domain.Contact;
import fr.insee.survey.datacollectionmanagement.contact.domain.Contact.Gender;
import fr.insee.survey.datacollectionmanagement.contact.service.AddressService;
import fr.insee.survey.datacollectionmanagement.contact.service.ContactService;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Campaign;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Owner;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Partitioning;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Source;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Support;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Survey;
import fr.insee.survey.datacollectionmanagement.metadata.dto.CampaignDto;
import fr.insee.survey.datacollectionmanagement.metadata.dto.MetadataDto;
import fr.insee.survey.datacollectionmanagement.metadata.dto.OwnerDto;
import fr.insee.survey.datacollectionmanagement.metadata.dto.PartitioningDto;
import fr.insee.survey.datacollectionmanagement.metadata.dto.SourceDto;
import fr.insee.survey.datacollectionmanagement.metadata.dto.SupportDto;
import fr.insee.survey.datacollectionmanagement.metadata.dto.SurveyDto;
import fr.insee.survey.datacollectionmanagement.metadata.service.CampaignService;
import fr.insee.survey.datacollectionmanagement.metadata.service.OwnerService;
import fr.insee.survey.datacollectionmanagement.metadata.service.PartitioningService;
import fr.insee.survey.datacollectionmanagement.metadata.service.SourceService;
import fr.insee.survey.datacollectionmanagement.metadata.service.SupportService;
import fr.insee.survey.datacollectionmanagement.metadata.service.SurveyService;
import fr.insee.survey.datacollectionmanagement.query.dto.ContactAccreditationDto;
import fr.insee.survey.datacollectionmanagement.query.dto.QuestioningProtoolsDto;
import fr.insee.survey.datacollectionmanagement.questioning.domain.Questioning;
import fr.insee.survey.datacollectionmanagement.questioning.domain.QuestioningAccreditation;
import fr.insee.survey.datacollectionmanagement.questioning.domain.QuestioningEvent;
import fr.insee.survey.datacollectionmanagement.questioning.domain.SurveyUnit;
import fr.insee.survey.datacollectionmanagement.questioning.dto.SurveyUnitDto;
import fr.insee.survey.datacollectionmanagement.questioning.service.QuestioningAccreditationService;
import fr.insee.survey.datacollectionmanagement.questioning.service.QuestioningEventService;
import fr.insee.survey.datacollectionmanagement.questioning.service.QuestioningService;
import fr.insee.survey.datacollectionmanagement.questioning.service.SurveyUnitService;
import fr.insee.survey.datacollectionmanagement.questioning.util.TypeQuestioningEvent;
import fr.insee.survey.datacollectionmanagement.view.service.ViewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@PreAuthorize("@AuthorizeMethodDecider.isInternalUser() "
        + "|| @AuthorizeMethodDecider.isWebClient() ")
@Slf4j
@Tag(name = "6 - Protools", description = "Enpoints for protools")
public class ProtoolsController {

    @Autowired
    QuestioningService questioningService;

    @Autowired
    SurveyUnitService surveyUnitService;

    @Autowired
    PartitioningService partitioningService;

    @Autowired
    SourceService sourceService;

    @Autowired
    SurveyService surveyService;

    @Autowired
    CampaignService campaignService;

    @Autowired
    OwnerService ownerService;

    @Autowired
    SupportService supportService;

    @Autowired
    ContactService contactService;

    @Autowired
    AddressService addressService;

    @Autowired
    ViewService viewService;

    @Autowired
    QuestioningAccreditationService questioningAccreditationService;

    @Autowired
    QuestioningEventService questioningEventService;

    @Autowired
    private ModelMapper modelMapper;

    @Operation(summary = "Create or update questioning for protools")
    @PutMapping(value = Constants.API_PROTOOLS_QUESTIONINGS, produces = "application/json", consumes = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(schema = @Schema(implementation = QuestioningProtoolsDto.class))),
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = QuestioningProtoolsDto.class))),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Bad request")

    })
    @Transactional
    public ResponseEntity<?> putQuestioning(@RequestBody QuestioningProtoolsDto questioningProtoolsDto)
            throws JsonMappingException, JsonProcessingException {

        log.info("Put questioning for protools {}", questioningProtoolsDto.toString());
        String modelName = questioningProtoolsDto.getModelName();
        String idSu = questioningProtoolsDto.getSurveyUnit().getIdSu();
        String idPartitioning = questioningProtoolsDto.getIdPartitioning();

        if (idPartitioning.isBlank() || modelName.isBlank() ||
                idSu.isBlank()) {
            log.warn("Partitioning {} does not exist", idPartitioning);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing fields");
        }

        if (!partitioningService.findById(idPartitioning).isPresent()) {
            log.warn("Partitioning {} does not exist", idPartitioning);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Partitioning does not exist");
        }
        SurveyUnit su;

        HttpStatus httpStatus = HttpStatus.OK;
        su = convertToEntity(questioningProtoolsDto.getSurveyUnit());

        // Create su if not exists or update
        try {
            SurveyUnit suBase = surveyUnitService.findbyId(idSu);
            su.setQuestionings(suBase.getQuestionings());
        } catch (NoSuchElementException e) {
            log.warn("survey unit {} does not exist - Creation of the survey unit",
                    idSu);

            su.setQuestionings(new HashSet<>());
        }
        surveyUnitService.saveSurveyUnitAndAddress(su);

        // Create questioning if not exists
        Questioning questioning = questioningService.findByIdPartitioningAndSurveyUnitIdSu(idPartitioning, idSu);
        if (questioning == null) {
            httpStatus = HttpStatus.CREATED;
            log.info("Create questioning for partitioning={} model={}  surveyunit={} ", idPartitioning, modelName,
                    idSu);
            questioning = new Questioning();
            questioning.setIdPartitioning(idPartitioning);
            questioning.setSurveyUnit(su);
            questioning.setModelName(modelName);
            QuestioningEvent questioningEvent = new QuestioningEvent();
            questioningEvent.setType(TypeQuestioningEvent.INITLA);
            questioningEvent.setDate(new Date());
            questioningEvent.setQuestioning(questioning);
            questioningEventService.saveQuestioningEvent(questioningEvent);
            questioning.setQuestioningEvents(new HashSet<>(Arrays.asList(questioningEvent)));
            questioning.setQuestioningAccreditations(new HashSet<>());
        }

        String json = "{\"author\":\"protools" + "\"}";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(json);
        for (ContactAccreditationDto contactAccreditationDto : questioningProtoolsDto.getContacts()) {
            // Create contact if not exists or update
            Contact contact;
            try {
                contact = convertToEntity(contactAccreditationDto);
                if (contactAccreditationDto.getAddress() != null)
                    contact.setAddress(addressService.convertToEntity(contactAccreditationDto.getAddress()));
                contactService.updateContactAddressEvent(contact, node);
            } catch (NoSuchElementException e) {
                log.info("Creating contact with the identifier {}", contactAccreditationDto.getIdentifier());
                contact = convertToEntityNewContact(contactAccreditationDto);
                if (contactAccreditationDto.getAddress() != null)
                    contact.setAddress(addressService.convertToEntity(contactAccreditationDto.getAddress()));
                contactService.createContactAddressEvent(contact, node);
            }

            // Create accreditations if not exists

            Set<QuestioningAccreditation> setExistingAccreditations = (questioning
                    .getQuestioningAccreditations() != null) ? questioning.getQuestioningAccreditations()
                            : new HashSet<>();
            Optional<Partitioning> part = partitioningService.findById(idPartitioning);

            List<QuestioningAccreditation> listContactAccreditations = setExistingAccreditations.stream()
                    .filter(acc -> acc.getIdContact().equals(contactAccreditationDto.getIdentifier())
                            && acc.getQuestioning().getIdPartitioning().equals(part.get().getId())
                            && acc.getQuestioning().getSurveyUnit().getIdSu().equals(idSu))
                    .collect(Collectors.toList());

            if (listContactAccreditations.isEmpty()) {
                // Create new accreditation
                QuestioningAccreditation questioningAccreditation = new QuestioningAccreditation();
                questioningAccreditation.setIdContact(contactAccreditationDto.getIdentifier());
                questioningAccreditation.setMain(contactAccreditationDto.isMain());
                questioningAccreditation.setQuestioning(questioning);
                setExistingAccreditations.add(questioningAccreditation);
                questioningAccreditationService.saveQuestioningAccreditation(questioningAccreditation);
                questioningService.saveQuestioning(questioning);

                // create view
                viewService.createView(contactAccreditationDto.getIdentifier(), questioning.getSurveyUnit().getIdSu(),
                        part.get().getCampaign().getId());

                questioning.getQuestioningAccreditations().add(questioningAccreditation);
            } else {
                // update accreditation
                QuestioningAccreditation questioningAccreditation = listContactAccreditations.get(0);
                questioningAccreditation.setMain(contactAccreditationDto.isMain());
                questioningAccreditationService.saveQuestioningAccreditation(questioningAccreditation);
            }

        }

        // save questioning and su
        questioningService.saveQuestioning(questioning);
        su.getQuestionings().add(questioning);
        su = surveyUnitService.saveSurveyUnitAndAddress(su);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(HttpHeaders.LOCATION, ServletUriComponentsBuilder.fromCurrentRequest().toUriString());
        log.info("Put questioning for protools ok");
        return ResponseEntity.status(httpStatus).headers(responseHeaders).body(questioningProtoolsDto);

    }

    @Operation(summary = "Get questioning for protools")
    @GetMapping(value = Constants.API_PROTOOLS_QUESTIONINGS, produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = QuestioningProtoolsDto.class))),
            @ApiResponse(responseCode = "404", description = "Not found"),
    })
    public ResponseEntity<?> getQuestioning(@RequestParam(required = true) String modelName,
            @RequestParam(required = true) String idPartitioning,
            @RequestParam(required = true) String idSurveyUnit)
            throws JsonMappingException, JsonProcessingException {

        QuestioningProtoolsDto questioningProtoolsDto = new QuestioningProtoolsDto();

        HttpStatus httpStatus = HttpStatus.OK;

        Questioning questioning = questioningService.findByIdPartitioningAndSurveyUnitIdSu(idPartitioning,
                idSurveyUnit);
        if (questioning == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Questioning does not exist");
        }
        questioningProtoolsDto.setIdPartitioning(idPartitioning);
        questioningProtoolsDto.setIdPartitioning(idPartitioning);
        questioningProtoolsDto.setModelName(modelName);
        questioningProtoolsDto.setSurveyUnit(convertToDto(questioning.getSurveyUnit()));
        List<ContactAccreditationDto> listContactAccreditationDto = new ArrayList<>();
        questioning.getQuestioningAccreditations().stream()
                .forEach(acc -> listContactAccreditationDto
                        .add(convertToDto(contactService.findByIdentifier(acc.getIdContact()), acc.isMain())));
        questioningProtoolsDto.setContacts(listContactAccreditationDto);
        return ResponseEntity.status(httpStatus).body(questioningProtoolsDto);

    }

    @Operation(summary = "Search for a partitiong and metadata by partitioning id")
    @GetMapping(value = Constants.API_PROTOOLS_METADATA_ID, produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = MetadataDto.class))),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> getMetadata(@PathVariable("id") String id) {
        MetadataDto metadataDto = new MetadataDto();
        try {
            Optional<Partitioning> part = partitioningService.findById(StringUtils.upperCase(id));
            if (!part.isPresent()) {
                log.warn("partitioning {} does not exist", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("partitioning does not exist");
            }
            metadataDto.setPartitioningDto(convertToDto(part.get()));
            metadataDto.setCampaignDto(convertToDto(part.get().getCampaign()));
            metadataDto.setSurveyDto(convertToDto(part.get().getCampaign().getSurvey()));
            metadataDto.setSourceDto(convertToDto(part.get().getCampaign().getSurvey().getSource()));
            metadataDto.setOwnerDto(convertToDto(part.get().getCampaign().getSurvey().getSource().getOwner()));
            metadataDto.setSupportDto(convertToDto(part.get().getCampaign().getSurvey().getSource().getSupport()));
            return ResponseEntity.ok().body(metadataDto);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error");
        }

    }

    @Operation(summary = "Insert or update a partitiong and metadata by partitioning id")
    @PutMapping(value = Constants.API_PROTOOLS_METADATA_ID, produces = "application/json", consumes = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = MetadataDto.class))),
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(schema = @Schema(implementation = MetadataDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @Transactional
    public ResponseEntity<?> putMetadata(@PathVariable("id") String id,
            @RequestBody MetadataDto metadataDto) {
        try {
            if (StringUtils.isBlank(metadataDto.getPartitioningDto().getId())
                    || !metadataDto.getPartitioningDto().getId().equalsIgnoreCase(id)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("id and idPartitioning don't match");
            }
            MetadataDto metadataReturn = new MetadataDto();

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set(HttpHeaders.LOCATION,
                    ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(id).toUriString());
            HttpStatus httpStatus;

            if (partitioningService.findById(id).isPresent()) {
                log.info("Update partitioning with the id {}", id);
                partitioningService.findById(id);
                httpStatus = HttpStatus.OK;

            } else {
                log.info("Create partitioning with the id {}", id);
                httpStatus = HttpStatus.CREATED;
            }

            Owner owner = ownerService.insertOrUpdateOwner(convertToEntity(metadataDto.getOwnerDto()));
            Support support = supportService.insertOrUpdateSupport(convertToEntity(metadataDto.getSupportDto()));

            Source source = sourceService.insertOrUpdateSource(convertToEntity(metadataDto.getSourceDto()));

            Survey survey = convertToEntity(metadataDto.getSurveyDto());
            survey.setSource(source);
            survey = surveyService.insertOrUpdateSurvey(survey);

            Campaign campaign = convertToEntity(metadataDto.getCampaignDto());
            campaign.setSurvey(survey);
            campaign = campaignService.insertOrUpdateCampaign(campaign);

            Partitioning partitioning = convertToEntity(metadataDto.getPartitioningDto());
            partitioning.setCampaign(campaign);
            partitioning = partitioningService.insertOrUpdatePartitioning(partitioning);

            Set<Partitioning> partitionings = (campaign.getPartitionings() == null) ? new HashSet<>()
                    : campaign.getPartitionings();
            partitionings.add(partitioning);
            campaign.setPartitionings(partitionings);
            campaign = campaignService.insertOrUpdateCampaign(campaign);

            Set<Campaign> campaigns = (survey.getCampaigns() == null) ? new HashSet<>()
                    : survey.getCampaigns();
            campaigns.add(campaign);
            survey.setCampaigns(campaigns);
            survey = surveyService.insertOrUpdateSurvey(survey);

            Set<Survey> surveys = (source.getSurveys() == null) ? new HashSet<>()
                    : source.getSurveys();
            surveys.add(survey);
            source.setSurveys(surveys);
            source.setOwner(owner);
            source.setSupport(support);

            source = sourceService.insertOrUpdateSource(source);

            Set<Source> sourcesOwner = (owner.getSources() == null) ? new HashSet<>()
                    : owner.getSources();
            sourcesOwner.add(source);
            owner.setSources(sourcesOwner);
            owner = ownerService.insertOrUpdateOwner(owner);

            Set<Source> sourcesSupport = (support.getSources() == null) ? new HashSet<>()
                    : support.getSources();
            sourcesSupport.add(source);
            support.setSources(sourcesSupport);
            support = supportService.insertOrUpdateSupport(support);

            metadataReturn.setOwnerDto(convertToDto(owner));
            metadataReturn.setSupportDto(convertToDto(support));
            metadataReturn.setSourceDto(convertToDto(source));
            metadataReturn.setSurveyDto(convertToDto(survey));
            metadataReturn.setCampaignDto(convertToDto(campaign));
            metadataReturn.setPartitioningDto(convertToDto(partitioning));

            return ResponseEntity.status(httpStatus).headers(responseHeaders).body(metadataReturn);
        } catch (Exception e) {
            log.error("Error in put metadata {}", metadataDto.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error");

        }

    }

    @Operation(summary = "Indicates whether a questioning should be follow up or not")
    @GetMapping(value = Constants.API_PROTOOLS_FOLLOWUP, produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = MetadataDto.class))),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> isToFollwUp(
            @PathVariable("idPartitioning") String idPartitioning,
            @PathVariable("idSu") String idSu) {

        Questioning questioning = questioningService.findByIdPartitioningAndSurveyUnitIdSu(
                idPartitioning, idSu);
        if (questioning == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Questioning does not exist");
        }

        Optional<QuestioningEvent> questioningEvent = questioningEventService.getLastQuestioningEvent(questioning,
                TypeQuestioningEvent.VALINT,
                TypeQuestioningEvent.VALPAP,
                TypeQuestioningEvent.REFUSAL,
                TypeQuestioningEvent.WASTE,
                TypeQuestioningEvent.HC);

        return ResponseEntity.status(HttpStatus.OK).body((questioningEvent.isPresent() ? "false" : "true"));
    }

    @Operation(summary = "Indicates whether a questioning should be extract or not (VALINT and PARTIELINT)")
    @GetMapping(value = Constants.API_PROTOOLS_EXTRACT, produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = MetadataDto.class))),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> isToExtract(@PathVariable("idPartitioning") String idPartitioning,
            @PathVariable("idSu") String idSu) {

        Questioning questioning = questioningService.findByIdPartitioningAndSurveyUnitIdSu(
                idPartitioning, idSu);
        if (questioning == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Questioning does not exist");
        }

        Optional<QuestioningEvent> questioningEvent = questioningEventService.getLastQuestioningEvent(questioning,
                TypeQuestioningEvent.VALINT,
                TypeQuestioningEvent.PARTIELINT);

        return ResponseEntity.status(HttpStatus.OK).body((questioningEvent.isPresent() ? "true" : "false"));
    }

    private Support convertToEntity(SupportDto supportDto) {
        return modelMapper.map(supportDto, Support.class);
    }

    private Owner convertToEntity(OwnerDto ownerDto) {
        return modelMapper.map(ownerDto, Owner.class);
    }

    private Source convertToEntity(SourceDto sourceDto) {
        return modelMapper.map(sourceDto, Source.class);
    }

    private Survey convertToEntity(SurveyDto surveyDto) {
        return modelMapper.map(surveyDto, Survey.class);
    }

    private Campaign convertToEntity(CampaignDto campaignDto) {
        return modelMapper.map(campaignDto, Campaign.class);
    }

    private Partitioning convertToEntity(PartitioningDto partitioningDto) {
        return modelMapper.map(partitioningDto, Partitioning.class);
    }

    private SurveyUnit convertToEntity(SurveyUnitDto surveyUnitDto) {
        return modelMapper.map(surveyUnitDto, SurveyUnit.class);
    }

    private Contact convertToEntity(ContactAccreditationDto contactAccreditationDto) {
        Contact contact = modelMapper.map(contactAccreditationDto, Contact.class);
        contact.setGender(contactAccreditationDto.getCivility().equals("Mr") ? Gender.Male : Gender.Female);

        Contact oldContact = contactService.findByIdentifier(contactAccreditationDto.getIdentifier());
        contact.setComment(oldContact.getComment());
        contact.setAddress(oldContact.getAddress());
        contact.setContactEvents(oldContact.getContactEvents());

        return contact;
    }

    private Contact convertToEntityNewContact(ContactAccreditationDto contactAccreditationDto) {
        Contact contact = modelMapper.map(contactAccreditationDto, Contact.class);
        contact.setGender(contactAccreditationDto.getCivility().equals("Mr") ? Gender.Male : Gender.Female);
        return contact;
    }

    private ContactAccreditationDto convertToDto(Contact contact, boolean isMain) {
        ContactAccreditationDto contactAccreditationDto = modelMapper.map(contact, ContactAccreditationDto.class);
        String civility = contact.getGender().equals(Gender.Male) ? "Mr" : "Mme";
        contactAccreditationDto.setCivility(civility);
        contactAccreditationDto.setMain(isMain);
        return contactAccreditationDto;
    }

    private SupportDto convertToDto(Support support) {
        return modelMapper.map(support, SupportDto.class);
    }

    private SurveyUnitDto convertToDto(SurveyUnit surveyUnit) {
        return modelMapper.map(surveyUnit, SurveyUnitDto.class);
    }

    private OwnerDto convertToDto(Owner owner) {
        return modelMapper.map(owner, OwnerDto.class);
    }

    private SourceDto convertToDto(Source source) {
        return modelMapper.map(source, SourceDto.class);
    }

    private SurveyDto convertToDto(Survey survey) {
        return modelMapper.map(survey, SurveyDto.class);
    }

    private CampaignDto convertToDto(Campaign campaign) {
        return modelMapper.map(campaign, CampaignDto.class);
    }

    private PartitioningDto convertToDto(Partitioning partitioning) {
        return modelMapper.map(partitioning, PartitioningDto.class);
    }

}
