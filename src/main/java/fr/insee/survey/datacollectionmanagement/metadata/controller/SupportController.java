package fr.insee.survey.datacollectionmanagement.metadata.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import fr.insee.survey.datacollectionmanagement.constants.Constants;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Support;
import fr.insee.survey.datacollectionmanagement.metadata.dto.SupportDto;
import fr.insee.survey.datacollectionmanagement.metadata.service.SupportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin
@Slf4j
@PreAuthorize("@AuthorizeMethodDecider.isInternalUser() "
        + "|| @AuthorizeMethodDecider.isWebClient() ")
@Tag(name = "3 - Metadata", description = "Enpoints to create, update, delete and find entities in metadata domain")
public class SupportController {

    @Autowired
    private ModelMapper modelmapper;

    @Autowired
    private SupportService supportService;

    @Operation(summary = "Search for supports, paginated")
    @GetMapping(value = Constants.API_SUPPORTS, produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = SupportPage.class)))
    })
    public ResponseEntity<?> getSupports(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(defaultValue = "id") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<Support> pageSupport = supportService.findAll(pageable);
        List<SupportDto> listSupports = pageSupport.stream().map(c -> convertToDto(c)).collect(Collectors.toList());
        return ResponseEntity.ok().body(new SupportPage(listSupports, pageable, pageSupport.getTotalElements()));
    }

    @Operation(summary = "Search for a support by its id")
    @GetMapping(value = Constants.API_SUPPORTS_ID, produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = SupportDto.class))),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> getSupport(@PathVariable("id") String id) {
        Optional<Support> support = supportService.findById(id);
        if (!support.isPresent()) {
            log.warn("Support {} does not exist", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("support does not exist");
        }
        support = supportService.findById(id);
        return ResponseEntity.ok().body(convertToDto(support.orElse(null)));

    }

    @Operation(summary = "Update or create a support")
    @PutMapping(value = Constants.API_SUPPORTS_ID, produces = "application/json", consumes = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = SupportDto.class))),
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(schema = @Schema(implementation = SupportDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> putSupport(@PathVariable("id") String id, @RequestBody SupportDto supportDto) {
        if (!supportDto.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("id and support id don't match");
        }

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(HttpHeaders.LOCATION,
                ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(supportDto.getId()).toUriString());
        HttpStatus httpStatus;

        log.warn("Update support with the id {}", supportDto.getId());
        Optional<Support> supportBase = supportService.findById(id);
        httpStatus = HttpStatus.OK;

        if (!supportBase.isPresent()) {
            log.info("Create support with the id {}", supportDto.getId());
            httpStatus = HttpStatus.CREATED;
        }

        Support support = supportService.insertOrUpdateSupport(convertToEntity(supportDto));
        return ResponseEntity.status(httpStatus).headers(responseHeaders).body(convertToDto(support));
    }

    private SupportDto convertToDto(Support support) {
        return modelmapper.map(support, SupportDto.class);
    }

    private Support convertToEntity(SupportDto supportDto) {
        return modelmapper.map(supportDto, Support.class);
    }

    class SupportPage extends PageImpl<SupportDto> {

        public SupportPage(List<SupportDto> content, Pageable pageable, long total) {
            super(content, pageable, total);
        }
    }

}
