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
import fr.insee.survey.datacollectionmanagement.metadata.domain.Owner;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Source;
import fr.insee.survey.datacollectionmanagement.metadata.dto.OwnerDto;
import fr.insee.survey.datacollectionmanagement.metadata.dto.SurveyDto;
import fr.insee.survey.datacollectionmanagement.metadata.service.OwnerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin
@Slf4j
@PreAuthorize("@AuthorizeMethodDecider.isInternalUser() "
        + "|| @AuthorizeMethodDecider.isWebClient() ")
@Tag(name = "3 - Metadata", description = "Enpoints to create, update, delete and find entities in metadata domain")
public class OwnerController {

    @Autowired
    private ModelMapper modelmapper;

    @Autowired
    private OwnerService ownerService;

    @Operation(summary = "Search for owners, paginated")
    @GetMapping(value = Constants.API_OWNERS, produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = OwnerPage.class)))
    })
    public ResponseEntity<?> getOwners(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(defaultValue = "id") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<Owner> pageOwner = ownerService.findAll(pageable);
        List<OwnerDto> listOwners = pageOwner.stream().map(c -> convertToDto(c)).collect(Collectors.toList());
        return ResponseEntity.ok().body(new OwnerPage(listOwners, pageable, pageOwner.getTotalElements()));
    }

    @Operation(summary = "Search for a owner by its id")
    @GetMapping(value = Constants.API_OWNERS_ID, produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = OwnerDto.class))),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> getOwner(@PathVariable("id") Long id) {
        Optional<Owner> owner = ownerService.findById(id);
        if (!owner.isPresent()) {
            log.warn("Owner {} does not exist", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("owner does not exist");
        }
        owner = ownerService.findById(id);
        return ResponseEntity.ok().body(convertToDto(owner.orElse(null)));

    }

    @Operation(summary = "Update or create a owner")
    @PutMapping(value = Constants.API_OWNERS_ID, produces = "application/json", consumes = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = OwnerDto.class))),
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(schema = @Schema(implementation = OwnerDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> putOwner(@PathVariable("id") Long id, @RequestBody OwnerDto ownerDto) {
        if (!ownerDto.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("id and owner id don't match");
        }

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(HttpHeaders.LOCATION,
                ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(ownerDto.getId()).toUriString());
        HttpStatus httpStatus;

        log.warn("Update owner with the id {}", ownerDto.getId());
        Optional<Owner> ownerBase = ownerService.findById(id);
        httpStatus = HttpStatus.OK;

        if (!ownerBase.isPresent()) {
            log.info("Create owner with the id {}", ownerDto.getId());
            httpStatus = HttpStatus.CREATED;
        }

        Owner owner = ownerService.insertOrUpdateOwner(convertToEntity(ownerDto));
        return ResponseEntity.status(httpStatus).headers(responseHeaders).body(convertToDto(owner));
    }
    

    private OwnerDto convertToDto(Owner owner) {
        return modelmapper.map(owner, OwnerDto.class);
    }

    private Owner convertToEntity(OwnerDto ownerDto) {
        return modelmapper.map(ownerDto, Owner.class);
    }

    class OwnerPage extends PageImpl<OwnerDto> {

        public OwnerPage(List<OwnerDto> content, Pageable pageable, long total) {
            super(content, pageable, total);
        }
    }

}
