package fr.insee.survey.datacollectionmanagement.user.controller;


import fr.insee.survey.datacollectionmanagement.constants.Constants;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Partitioning;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Source;
import fr.insee.survey.datacollectionmanagement.metadata.service.SourceService;
import fr.insee.survey.datacollectionmanagement.questioning.domain.Questioning;
import fr.insee.survey.datacollectionmanagement.questioning.domain.QuestioningAccreditation;
import fr.insee.survey.datacollectionmanagement.questioning.dto.QuestioningAccreditationDto;
import fr.insee.survey.datacollectionmanagement.user.domain.SourceAccreditation;
import fr.insee.survey.datacollectionmanagement.user.dto.SourceAccreditationDto;
import fr.insee.survey.datacollectionmanagement.user.service.SourceAccreditationService;
import fr.insee.survey.datacollectionmanagement.user.service.UserService;
import fr.insee.survey.datacollectionmanagement.view.service.ViewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@PreAuthorize("@AuthorizeMethodDecider.isInternalUser() "
        + "|| @AuthorizeMethodDecider.isWebClient() ")
@Tag(name = "7-User", description = "Enpoints to create, update, delete and find users, their events and accreditations")
public class SourceAccreditationController {

    static final Logger LOGGER = LoggerFactory.getLogger(SourceAccreditationController.class);

    @Autowired
    private SourceAccreditationService sourceAccreditationService;

    @Autowired
    private SourceService sourceService;

    @Autowired
    private UserService userService;

    @Autowired
    private ViewService viewService;

    @Autowired
    private ModelMapper modelMapper;

    @Operation(summary = "Search for source accreditations by source id")
    @GetMapping(value = Constants.API_SOURCE_ID_SOURCE_ACCREDITATIONS, produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = SourceAccreditationDto.class)))),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    public ResponseEntity<?> getSourceAccreditation(@PathVariable("id") String id) {

        try {
            Optional<Source> optSource = sourceService.findById(id);
            if (optSource.isPresent())
                return new ResponseEntity<>(
                        optSource.get().getSourceAccreditations().stream().map(c -> convertToDto(c))
                                .collect(Collectors.toList()),
                        HttpStatus.OK);
            else
                return new ResponseEntity<>("Source does not exist", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<String>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Create or update a source accreditation for a source")
    @PostMapping(value = Constants.API_SOURCE_ID_SOURCE_ACCREDITATIONS, produces = "application/json", consumes = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",

                    content = @Content(schema = @Schema(implementation = SourceAccreditationDto.class))),
            @ApiResponse(responseCode = "404", description = "NotFound")
    })
    @Transactional
    public ResponseEntity<?> postSourceAccreditation(@PathVariable("id") String id,
                                                          @RequestBody SourceAccreditationDto sourceAccreditationDto) {

        Optional<Source> optSource = null;

        String idUser = sourceAccreditationDto.getIdUser();

        // Check if questioning exists
        try {
            optSource = sourceService.findById(id);
            if (!optSource.isPresent())
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Source does not exist");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
        Source source = optSource.get();

        // Check if contact exists
        if (!userService.findByIdentifier(idUser).isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist");

        HttpHeaders responseHeaders = new HttpHeaders();

        // save new accreditation or update existing one
        Set<SourceAccreditation> setExistingAccreditations = source.getSourceAccreditations();

        List<SourceAccreditation> listUserAccreditations = setExistingAccreditations.stream()
                .filter(acc -> acc.getIdUser().equals(idUser))
                .collect(Collectors.toList());

        if (listUserAccreditations.isEmpty()) {
            // Create new accreditation
            SourceAccreditation sourceAccreditation = convertToEntity(sourceAccreditationDto);
            sourceAccreditation.setSource(source);
            setExistingAccreditations.add(sourceAccreditation);
            sourceAccreditationService.saveSourceAccreditation(sourceAccreditation);
            source.setSourceAccreditations(setExistingAccreditations);
            sourceService.insertOrUpdateSource(source);

            // location header
            responseHeaders.set(HttpHeaders.LOCATION,
                    ServletUriComponentsBuilder.fromCurrentRequest().path(sourceAccreditation.getId().toString())
                            .toUriString());

            return ResponseEntity.status(HttpStatus.CREATED).headers(responseHeaders)
                    .body(convertToDto(sourceAccreditation));

        } else {
            // update accreditation
            SourceAccreditation sourceAccreditation = listUserAccreditations.get(0);
            sourceAccreditationDto.setId(sourceAccreditation.getId());
            sourceAccreditation = convertToEntity(sourceAccreditationDto);
            sourceAccreditation.setSource(source);
            sourceAccreditationService.saveSourceAccreditation(sourceAccreditation);

            // location header
            responseHeaders.set(HttpHeaders.LOCATION,
                    ServletUriComponentsBuilder.fromCurrentRequest().path(sourceAccreditation.getId().toString())
                            .toUriString());
            return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders)
                    .body(convertToDto(sourceAccreditation));
        }

    }

    private SourceAccreditation convertToEntity(SourceAccreditationDto sourceAccreditationDto) {
        return modelMapper.map(sourceAccreditationDto, SourceAccreditation.class);
    }

    private SourceAccreditationDto convertToDto(SourceAccreditation sourceAccreditation) {
        return modelMapper.map(sourceAccreditation, SourceAccreditationDto.class);
    }

}

