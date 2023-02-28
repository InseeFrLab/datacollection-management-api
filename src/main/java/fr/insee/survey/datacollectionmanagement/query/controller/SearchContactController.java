package fr.insee.survey.datacollectionmanagement.query.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.insee.survey.datacollectionmanagement.constants.Constants;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Partitioning;
import fr.insee.survey.datacollectionmanagement.metadata.service.PartitioningService;
import fr.insee.survey.datacollectionmanagement.query.dto.AccreditationDetailDto;
import fr.insee.survey.datacollectionmanagement.query.dto.SearchContactDto;
import fr.insee.survey.datacollectionmanagement.query.service.SearchContactService;
import fr.insee.survey.datacollectionmanagement.questioning.domain.Questioning;
import fr.insee.survey.datacollectionmanagement.questioning.domain.QuestioningAccreditation;
import fr.insee.survey.datacollectionmanagement.questioning.service.QuestioningAccreditationService;
import fr.insee.survey.datacollectionmanagement.view.domain.View;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@PreAuthorize("@AuthorizeMethodDecider.isInternalUser() "
        + "|| @AuthorizeMethodDecider.isWebClient() "
        + "|| @AuthorizeMethodDecider.isAdmin() ")
@Tag(name = "4 - Cross domain")
@Slf4j
public class SearchContactController {

    @Autowired
    private SearchContactService searchContactService;

    @Autowired
    private QuestioningAccreditationService questioningAccreditationService;

    @Autowired
    private PartitioningService partitioningService;

    @GetMapping(path = Constants.API_CONTACTS_SEARCH, produces = "application/json")
    @Operation(summary = "Multi-criteria search contacts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = SearchContactDto.class)))),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    public ResponseEntity<?> searchContacts(
            @RequestParam(required = false) String identifier,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String idSu,
            @RequestParam(required = false) String identificationCode,
            @RequestParam(required = false) String identificationName,
            @RequestParam(required = false) String source,
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String period,
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize) {

        log.info(
                "Search contact: identifier = {}, lastName= {}, firstName= {}, email= {}, idSu= {}, identificationCode= {}, identificationName= {}, source= {}, year= {}, period= {}, pageNo= {}, pageSize= {} ",
                identifier, lastName, firstName, email, idSu, identificationCode, identificationName, source, year,
                period, pageNo, pageSize);

        Pageable pageable = PageRequest.of(pageNo, pageSize);

        List<View> listView = searchContactService.searchContactCrossDomain(identifier, lastName, firstName, email,
                idSu, identificationCode, identificationName, source, year, period,
                pageable);
        int start = (int) pageable.getOffset();
        int end = (int) ((start + pageable.getPageSize()) > listView.size() ? listView.size()
                : (start + pageable.getPageSize()));

        if (listView.isEmpty()) {
            return new ResponseEntity<>(Collections.EMPTY_LIST, HttpStatus.NOT_FOUND);
        }
        if (start <= end) {
            Page<SearchContactDto> page = new PageImpl<SearchContactDto>(
                    searchContactService.transformListViewDaoToDto(listView.subList(start, end)), pageable,
                    listView.size());
            return new ResponseEntity<>(page, HttpStatus.OK);

        }

        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

    @GetMapping(path = Constants.API_CONTACTS_ACCREDITATIONS, produces = "application/json")
    @Operation(summary = "Get contact accreditations by the contact id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = AccreditationDetailDto.class)))),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    public ResponseEntity<?> getContactAccreditations(@PathVariable("id") String id) {

        List<AccreditationDetailDto> listAccreditations = new ArrayList<>();
        List<QuestioningAccreditation> accreditations = questioningAccreditationService.findByContactIdentifier(id);
        for (QuestioningAccreditation questioningAccreditation : accreditations) {
            Questioning questioning = questioningAccreditation.getQuestioning();
            Optional<Partitioning> part = partitioningService.findById(questioning.getIdPartitioning());

            listAccreditations.add(new AccreditationDetailDto(part.get().getCampaign().getSurvey().getSource().getId(),
                    part.get().getCampaign().getSurvey().getSource().getShortWording(),
                    part.get().getCampaign().getSurvey().getYear(), part.get().getCampaign().getPeriod(),
                    part.get().getId(), questioningAccreditation.getQuestioning().getSurveyUnit().getIdSu(),
                    questioningAccreditation.getQuestioning().getSurveyUnit().getIdentificationName(),
                    questioningAccreditation.isMain()));

        }

        return new ResponseEntity<>(listAccreditations, HttpStatus.OK);

    }

}
