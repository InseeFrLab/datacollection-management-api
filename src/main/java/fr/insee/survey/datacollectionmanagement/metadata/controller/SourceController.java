package fr.insee.survey.datacollectionmanagement.metadata.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import fr.insee.survey.datacollectionmanagement.constants.Constants;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Campaign;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Partitioning;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Source;
import fr.insee.survey.datacollectionmanagement.metadata.dto.SourceDto;
import fr.insee.survey.datacollectionmanagement.metadata.service.SourceService;
import fr.insee.survey.datacollectionmanagement.questioning.service.QuestioningService;
import fr.insee.survey.datacollectionmanagement.view.service.ViewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin
@Tag(name = "3 - Metadata", description = "Enpoints to create, update, delete and find entities in metadata domain")
public class SourceController {

    static final Logger LOGGER = LoggerFactory.getLogger(SourceController.class);

    @Autowired
    private SourceService sourceService;

    @Autowired
    private ViewService viewService;

    @Autowired
    private ModelMapper modelmapper;

    private QuestioningService questioningService;

    @Operation(summary = "Search for sources, paginated")
    @GetMapping(value = Constants.API_SOURCES, produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = SourcePage.class)))
    })
    public ResponseEntity<?> getSources(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(defaultValue = "id") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<Source> pageSource = sourceService.findAll(pageable);
        List<SourceDto> listSources = pageSource.stream().map(c -> convertToDto(c)).collect(Collectors.toList());
        return ResponseEntity.ok().body(new SourcePage(listSources, pageable, pageSource.getTotalElements()));
    }

    @Operation(summary = "Search for a source by its id")
    @GetMapping(value = Constants.API_SOURCES_ID, produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = SourceDto.class))),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> getSource(@PathVariable("id") String id) {
        Source source = null;
        try {
            source = sourceService.findById(StringUtils.upperCase(id));
            return ResponseEntity.ok().body(convertToDto(source));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("source does not exist");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error");
        }

    }

    @Operation(summary = "Update or create a source")
    @PutMapping(value = Constants.API_SOURCES_ID, produces = "application/json", consumes = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = SourceDto.class))),
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(schema = @Schema(implementation = SourceDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> putSource(@PathVariable("id") String id, @RequestBody SourceDto sourceDto) {
        if (StringUtils.isBlank(sourceDto.getId()) || !sourceDto.getId().equalsIgnoreCase(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("id and source id don't match");
        }

        Source source;
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(HttpHeaders.LOCATION,
                ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(sourceDto.getId()).toUriString());
        HttpStatus httpStatus;

        try {
            sourceService.findById(id);
            httpStatus = HttpStatus.OK;

        } catch (NoSuchElementException e) {
            LOGGER.info("Creating source with the id {}", sourceDto.getId());
            httpStatus = HttpStatus.CREATED;
        }

        source = sourceService.updateSource(convertToEntity(sourceDto));
        return ResponseEntity.status(httpStatus).headers(responseHeaders).body(convertToDto(source));
    }

    @Operation(summary = "Delete a source, its surveys, campaigns, partitionings, questionings ...")
    @DeleteMapping(value = Constants.API_SOURCES_ID)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @Transactional
    public ResponseEntity<?> deleteSource(@PathVariable("id") String id) {
        try {
            Source source = sourceService.findById(id);
            sourceService.deleteSourceById(id);
            List<Campaign> listCampaigns = new ArrayList<>();
            List<Partitioning> listPartitionings = new ArrayList<>();
            
            source.getSurveys().stream().forEach(su -> listCampaigns.addAll(su.getCampaigns()));
            source.getSurveys().stream().forEach(su -> su.getCampaigns().stream().forEach(c ->listPartitionings.addAll(c.getPartitionings())));


            for (Campaign campaign : listCampaigns) {
                viewService.findViewByCampaignId(campaign.getCampaignId()).stream().forEach(v -> viewService.deleteView(v));
            }
            for (Partitioning partitioning : listPartitionings) {
                questioningService.findByIdPartitioning(partitioning.getId()).stream()
                .forEach(q -> questioningService.deleteQuestioning(q.getId()));
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Source deleted");
        } catch (

        NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Source does not exist");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error");
        }
    }

    private SourceDto convertToDto(Source source) {
        return modelmapper.map(source, SourceDto.class);
    }

    private Source convertToEntity(SourceDto sourceDto) {
        return modelmapper.map(sourceDto, Source.class);
    }

    class SourcePage extends PageImpl<SourceDto> {

        public SourcePage(List<SourceDto> content, Pageable pageable, long total) {
            super(content, pageable, total);
        }
    }

}
